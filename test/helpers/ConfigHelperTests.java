package helpers;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.typesafe.config.Config;

import java.util.Arrays;
import java.util.List;

public class ConfigHelperTests {

    private ConfigHelper sut;

    @Before
    public void setUp() {
        Config configuration = mock(Config.class);
        when(configuration.getString(anyString())).thenReturn("fake_string");
        when(configuration.getStringList(anyString())).thenReturn(Arrays.asList("fake_string", "fake_string"));
        when(configuration.getBoolean(anyString())).thenReturn(true);

        this.sut = new ConfigHelper(configuration);
    }

    @Test
    public void test_getConfigString() {
        String actual = this.sut.getConfigString("keytest");
        assertThat(actual).isEqualTo("fake_string");
    }

    @Test
    public void test_getStringList() {
        List<String> actual = this.sut.getStringList("keytest");
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    public void test_getConfigBoolean() {
        Boolean actual = this.sut.getConfigBoolean("keytest");
        assertThat(actual).isEqualTo(true);
    }

    @Test
    public void test_getEnvironment() {
        String actual = this.sut.getEnvironment();
        assertThat(actual).isEqualTo("fake_string");
    }
}

