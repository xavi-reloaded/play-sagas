package adapters;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dtos.DefaultAWSCredentials;
import dtos.FileStream;
import exceptions.AmazonException;
import helpers.ConfigHelper;
import play.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by ecobos on 9/20/16.
 */
@Singleton
public class S3Adapter implements IS3Adapter {

    String bucketName;

    @Inject
    public S3Adapter(ConfigHelper configHelper){
        this.bucketName = "##s3bucketname##";
    }

    @Override
    public Boolean uploadFile(File file, String keyName, Boolean publicFile) throws AmazonException {
        AmazonS3 s3client = this.getClient();
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, keyName, file);
            if (publicFile){
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            s3client.putObject(putObjectRequest);
            return true;
        } catch (AmazonClientException ase) {
            throw new AmazonException(ase.getMessage());
        }
    }

    @Override
    public Boolean uploadBufferedImage(BufferedImage image, String keyName) throws IOException {
        AmazonS3 s3client = this.getClient();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        byte[] buffer = os.toByteArray();
        InputStream is = new ByteArrayInputStream(buffer);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(buffer.length);
        meta.setContentType("image/jpg");
        try {
            s3client.putObject(new PutObjectRequest(this.bucketName, keyName, is, meta).withCannedAcl(CannedAccessControlList.PublicRead));
            return true;
        } catch (AmazonClientException e) {
            Logger.error(this.getClass().getSimpleName(), e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean deleteFile(String keyName) {
        AmazonS3 s3client = this.getClient();
        s3client.deleteObject(new DeleteObjectRequest(this.bucketName, keyName));
        return true;
    }

    @Override
    public FileStream getFile(String key){
        AmazonS3 s3Client = this.getClient();
        S3Object s3object = s3Client.getObject(new GetObjectRequest(this.bucketName, key));
        return new FileStream(s3object.getObjectContent(), s3object.getObjectMetadata().getContentType());
    }

    private AmazonS3 getClient(){
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new DefaultAWSCredentials()))
                .withRegion(DefaultAWSCredentials.AWS_REGION)
                .build();
    }

    @Override
    public void createFile(String fileKey, String fileContent){
        this.getClient().putObject(this.bucketName, fileKey, fileContent);
    }
}
