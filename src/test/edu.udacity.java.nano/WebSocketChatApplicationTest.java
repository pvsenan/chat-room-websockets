package edu.udacity.java.nano;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketChatApplicationTest {

    @LocalServerPort
    int randomServerPort;

    private WebDriver driverOne;
    private WebDriver driverTwo;

    @Before
    public void setUp() throws Exception {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriverManager.chromedriver().setup();
        driverOne = new ChromeDriver(chromeOptions);
        driverOne.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
        driverTwo = new ChromeDriver(chromeOptions);
        driverTwo.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);

        driverOne.navigate().to("http://localhost:" + String.valueOf(randomServerPort));
        driverTwo.navigate().to("http://localhost:" + String.valueOf(randomServerPort));
    }

    @After
    public void tearDown() throws Exception {
        driverOne.quit();
        driverTwo.quit();
    }

    @Test
    public void userCanLoginToApplication(){
        String username = "August";
        login(username, driverOne);
        Assert.assertEquals(username, driverOne.findElement(By.cssSelector("#username")).getText());
        org.junit.Assert.assertTrue(true);
    }

    @Test
    public void userCanSendMessage(){
        String username = "August";
        String message = "Hello everyone, how are you guys";
        login(username, driverOne);
        sendMessage(message, driverOne);
        Assert.assertEquals(1, driverOne.findElements(By.cssSelector(".message-content")).size());
    }

    @Test
    public void userCountIsIncremenetedWhenNewMembersJoin() {
        String userOne = "August";
        String userTwo = "June";
        login(userOne, driverOne);
        login(userTwo, driverTwo);
        Assert.assertEquals("2", driverOne.findElement(By.cssSelector(".chat-num")).getText());
    }

    @Test
    public void userCountIsDecrementedWhenNewMembersLeaves() {
        String userOne = "August";
        String userTwo = "June";
        login(userOne, driverOne);
        login(userTwo, driverTwo);
        Assert.assertEquals("2", driverOne.findElement(By.cssSelector(".chat-num")).getText());
        driverTwo.findElement(By.cssSelector("#exit")).click();
        Assert.assertEquals("1", driverOne.findElement(By.cssSelector(".chat-num")).getText());
    }

    @Test
    public void usersCanBroadcastMessage(){
        String userOne = "August";
        String userTwo = "June";
        String messageOne = "Hello everyone, how are you guys";
        String messageTwo = "Fine guys";
        login(userOne, driverOne);
        login(userTwo, driverTwo);
        sendMessage(messageOne, driverOne);
        sendMessage(messageTwo, driverTwo);
    }

    private void login(String username, WebDriver driver){
        driver.findElement(By.cssSelector("#username")).sendKeys(username);
        driver.findElement(By.cssSelector(".submit")).click();
    }

    private void sendMessage(String message, WebDriver driver){
        driver.findElement(By.cssSelector("#msg")).sendKeys(message);
        driver.findElement(By.cssSelector("#sendmessage")).click();
    }
}