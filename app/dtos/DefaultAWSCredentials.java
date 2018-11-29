package dtos;

import com.amazonaws.auth.BasicAWSCredentials;

public class DefaultAWSCredentials extends BasicAWSCredentials {

    public static final String AWS_REGION = "##aws_region##";

    public DefaultAWSCredentials(){
        super("##aws_accesskey##", "##aws_secret_key##");
    }
}
