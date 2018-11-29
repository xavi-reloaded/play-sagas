package helpers.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import helpers.validator.dtos.ValidatorObjectDTO;
import helpers.validator.enumerations.ValidatorsEnum;
import helpers.validator.exceptions.*;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import play.libs.Json;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(DataProviderRunner.class)
public class ValidatorHelperTests {

    private ValidatorHelper sut;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        this.sut = new ValidatorHelper();
    }

    @DataProvider
    public static Object[][] dataProviderValidate() {
        return new Object[][]{
                {new JSONObject().put("key1", ""), ValidatorHelperTests.getValidators(1), ValidatorHelperTests.getExpectedValidate(1)},
                {new JSONObject().put("key1", "value1"), ValidatorHelperTests.getValidators(2), ValidatorHelperTests.getExpectedValidate(2)},
                {new JSONObject().put("key1", "email@email.com"), ValidatorHelperTests.getValidators(3), ValidatorHelperTests.getExpectedValidate(3)},
                {new JSONObject().put("key1", "email@email.com"), ValidatorHelperTests.getValidators(4), ValidatorHelperTests.getExpectedValidate(4)},
                {new JSONObject().put("key1", "url"), ValidatorHelperTests.getValidators(5), ValidatorHelperTests.getExpectedValidate(5)},
                {new JSONObject().put("key1", "url").put("key2", "url2"), ValidatorHelperTests.getValidators(6), ValidatorHelperTests.getExpectedValidate(6)},
        };
    }

    @Test
    @UseDataProvider("dataProviderValidate")
    public void test_validate_void(JSONObject body, LinkedHashMap<String, List<ValidatorObjectDTO>> validators, List<IBaseValidationException> expected) throws IOException {

        ValidationErrorException actual = null;
        try {
            this.sut.validate(new ValidatorParams(this.objectMapper.readTree(body.toString()), validators));
        } catch (ValidationErrorException e) {
            actual = e;
        }

        assertThat(Json.toJson(actual.getBody().getErrors())).isEqualTo(Json.toJson(expected));

    }

    @Test
    public void test_validate_boolean() throws IOException, ValidationErrorException {
        LinkedHashMap<String, List<ValidatorObjectDTO>> validators = new LinkedHashMap<>();
        validators.put("key1", Collections.singletonList(new ValidatorObjectDTO(ValidatorsEnum.REQUIRED)));
        JSONObject body = new JSONObject().put("key1", "url");
        Boolean actual = this.sut.validate(new ValidatorParams(this.objectMapper.readTree(body.toString()), validators));
        assertThat(actual).isEqualTo(true);

    }

    @DataProvider
    public static Object[][] dataProviderValidateRequired() {
        return new Object[][]{
                {new JSONObject().put("key1", "a"), "key1", null},
                {new JSONObject().put("key1", ""), "key1", new RequiredValidation("key1")},
                {new JSONObject().put("key1", "a"), "key2", new RequiredValidation("key2")},
        };
    }

    @Test
    @UseDataProvider("dataProviderValidateRequired")
    public void test_validateRequired_void(JSONObject body, String key, IBaseValidationException expected) throws IOException {
        IBaseValidationException actual = this.sut.validateRequired(this.objectMapper.readTree(body.toString()), key);
        assertThat(Json.toJson(actual)).isEqualTo(Json.toJson(expected));
    }

    @DataProvider
    public static Object[][] dataProviderValidateEmail() {
        return new Object[][]{
                {new JSONObject().put("key1", ""), "key2", new IllegalAccessValidation("key2")},
                {new JSONObject().put("key1", ""), "key1", new EmailFormatValidation("key1")},
                {new JSONObject().put("key1", "email"), "key1", new EmailFormatValidation("key1")},
                {new JSONObject().put("key1", "email@"), "key1", new EmailFormatValidation("key1")},
                {new JSONObject().put("key1", "email@c"), "key1", new EmailFormatValidation("key1")},
                {new JSONObject().put("key1", "email@c."), "key1", new EmailFormatValidation("key1")},
                {new JSONObject().put("key1", "@c.com"), "key1", new EmailFormatValidation("key1")},
                {new JSONObject().put("key1", "a@.com"), "key1", new EmailFormatValidation("key1")},
                {new JSONObject().put("key1", ".com"), "key1", new EmailFormatValidation("key1")},
                {new JSONObject().put("key1", "email@c.c"), "key1", null},
                {new JSONObject().put("key1", "email@wearejust.digital"), "key1", null},
        };
    }

    @Test
    @UseDataProvider("dataProviderValidateEmail")
    public void test_validateEmail_void(JSONObject body, String key, IBaseValidationException expected) throws IOException {
        IBaseValidationException actual = this.sut.validateEmail(this.objectMapper.readTree(body.toString()), key);
        assertThat(Json.toJson(actual)).isEqualTo(Json.toJson(expected));
    }


    @DataProvider
    public static Object[][] dataProviderValidateURL() {
        return new Object[][]{
                {new JSONObject().put("key1", ""), "key2", new IllegalAccessValidation("key2")},
                {new JSONObject().put("key1", ""), "key1", new UrlFormatValidation("key1")},
                {new JSONObject().put("key1", "url"), "key1", new UrlFormatValidation("key1")},
                {new JSONObject().put("key1", "http:url"), "key1", new UrlFormatValidation("key1")},
                {new JSONObject().put("key1", "http://url^.com?aa=bb"), "key1", new UrlFormatValidation("key1")},
                {new JSONObject().put("key1", "http://url"), "key1", null},
                {new JSONObject().put("key1", "http://url.com"), "key1", null},
                {new JSONObject().put("key1", "http://url.com?aa=bb"), "key1", null},
        };
    }

    @Test
    @UseDataProvider("dataProviderValidateURL")
    public void test_validateURL_void(JSONObject body, String key, IBaseValidationException expected) throws IOException {
        IBaseValidationException actual = this.sut.validateURL(this.objectMapper.readTree(body.toString()), key);
        assertThat(Json.toJson(actual)).isEqualTo(Json.toJson(expected));
    }

    @DataProvider
    public static Object[][] dataProviderValidateEqualTo() {
        return new Object[][]{
                {new JSONObject().put("key1", "").put("key2", ""), "key3", "key4", new IllegalAccessValidation("key3")},
                {new JSONObject().put("key1", "").put("key2", ""), "key2", "key4", new IllegalAccessValidation("key4")},
                {new JSONObject(), "key2", "key4", new IllegalAccessValidation("key2")},
                {new JSONObject().put("key1", "a").put("key2", ""), "key1", "key2", new EqualToValidation("key1", "key2")},
                {new JSONObject().put("key1", "a").put("key2", "b"), "key1", "key2", new EqualToValidation("key1", "key2")},
                {new JSONObject().put("key1", "a").put("key2", "a"), "key1", "key2", null},
        };
    }

    @Test
    @UseDataProvider("dataProviderValidateEqualTo")
    public void test_validateEqualTo_void(JSONObject body, String key, String key2, IBaseValidationException expected) throws IOException {
        IBaseValidationException actual = this.sut.validateEqualTo(this.objectMapper.readTree(body.toString()), key, key2);
        assertThat(Json.toJson(actual)).isEqualTo(Json.toJson(expected));
    }

    @DataProvider
    public static Object[][] dataProviderValidateMinLength() {
        return new Object[][]{
                {new JSONObject().put("key1", "a"), "key2", 1, new IllegalAccessValidation("key2")},
                {new JSONObject().put("key1", ""), "key1", 2, null},
                {new JSONObject().put("key1", "a"), "key1", 2, new MinLengthValidation("key1", 2)},
                {new JSONObject().put("key1", "aa"), "key1", 2, null},
        };
    }

    @Test
    @UseDataProvider("dataProviderValidateMinLength")
    public void test_validateMinLength_void(JSONObject body, String key, Integer minLength, IBaseValidationException expected) throws IOException {
        IBaseValidationException actual = this.sut.validateMinLength(this.objectMapper.readTree(body.toString()), key, minLength);
        assertThat(Json.toJson(actual)).isEqualTo(Json.toJson(expected));
    }

    @DataProvider
    public static Object[][] dataProviderValidateMaxLength() {
        return new Object[][]{
                {new JSONObject().put("key1", "a"), "key2", 1, new IllegalAccessValidation("key2")},
                {new JSONObject().put("key1", "aaa"), "key1", 2, new MaxLengthValidation("key1", 2)},
                {new JSONObject().put("key1", "aa"), "key1", 2, null},
                {new JSONObject().put("key1", "a"), "key1", 2, null},
        };
    }

    @Test
    @UseDataProvider("dataProviderValidateMaxLength")
    public void test_validateMaxLength_void(JSONObject body, String key, Integer maxLength, IBaseValidationException expected) throws IOException {
        IBaseValidationException actual = this.sut.validateMaxLength(this.objectMapper.readTree(body.toString()), key, maxLength);
        assertThat(Json.toJson(actual)).isEqualTo(Json.toJson(expected));
    }

    private static LinkedHashMap<String, List<ValidatorObjectDTO>> getValidators(Integer step) {
        LinkedHashMap<String, List<ValidatorObjectDTO>> validators = new LinkedHashMap<>();

        switch (step) {
            case 1:
                validators.put("key1", Collections.singletonList(new ValidatorObjectDTO(ValidatorsEnum.REQUIRED)));
                break;
            case 2:
                validators.put("key1", Arrays.asList(new ValidatorObjectDTO(ValidatorsEnum.REQUIRED), new ValidatorObjectDTO(ValidatorsEnum.EMAIL)));
                break;
            case 3:
                validators.put("key1", Arrays.asList(new ValidatorObjectDTO(ValidatorsEnum.REQUIRED), new ValidatorObjectDTO(ValidatorsEnum.EMAIL), new ValidatorObjectDTO(ValidatorsEnum.MIN_LENGTH, 16)));
                break;
            case 4:
                validators.put("key1", Arrays.asList(new ValidatorObjectDTO(ValidatorsEnum.REQUIRED), new ValidatorObjectDTO(ValidatorsEnum.EMAIL), new ValidatorObjectDTO(ValidatorsEnum.MAX_LENGTH, 10)));
                break;
            case 5:
                validators.put("key1", Arrays.asList(new ValidatorObjectDTO(ValidatorsEnum.REQUIRED), new ValidatorObjectDTO(ValidatorsEnum.URL)));
                break;
            case 6:
                validators.put("key1", Arrays.asList(new ValidatorObjectDTO(ValidatorsEnum.REQUIRED), new ValidatorObjectDTO(ValidatorsEnum.EQUAL_TO, "key2")));
                break;
            default:
                break;

        }
        return validators;
    }


    private static List<IBaseValidationException> getExpectedValidate(Integer step) {
        List<IBaseValidationException> expected = new ArrayList<>();

        switch (step) {
            case 1:
                expected.add(new RequiredValidation("key1"));
                break;
            case 2:
                expected.add(new EmailFormatValidation("key1"));
                break;
            case 3:
                expected.add(new MinLengthValidation("key1", 16));
                break;
            case 4:
                expected.add(new MaxLengthValidation("key1", 10));
                break;
            case 5:
                expected.add(new UrlFormatValidation("key1"));
                break;
            case 6:
                expected.add(new EqualToValidation("key1", "key2"));
                break;
            default:
                break;

        }
        return expected;
    }
}