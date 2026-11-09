package demo.wrappers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */

    WebDriver driver;

    // A constructor is automatically called when you create an object using the new keyword
    public Wrappers(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateURL(String url) {
        driver.navigate().to(url);
    }

    public void waitForPageLoad(){

        JavascriptExecutor js = ((JavascriptExecutor) driver);

        boolean isLoaded = js.executeScript("return document.readyState")
                .toString()
                .equals("complete");

        if (isLoaded) {
            System.out.println("Page loaded successfully!");
        } else {
            System.out.println("Page is not loaded!");
        }
    }

    public void scroll(WebElement element){
        // true: Aligns element to the top,
        // false: Aligns element to the bottom
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }


    public void enterText(String textLocator, String actualText){

        try {
            // WebElement parent =
            // driver.findElement(By.xpath("//div[@jscontroller='sWGJ4b']/descendant::span[contains(normalize-space(.),'"
            // + textLocator + "')]"));

            WebElement parent = driver.findElement(
                    By.xpath("//div[@jscontroller='sWGJ4b']/descendant::span[contains(text(),'" + textLocator + "')]"));

            // Step 2: Check for input element
            List<WebElement> eleInput = parent.findElements(
                    By.xpath(
                            "./ancestor::div[@class='z12JJ']/following-sibling::div[@jscontroller='oCiKKc']/descendant::input[@type='text']"));

            // Step 3: Decide which child element to use
            /*
             * .// used for descendant
             * ./ used for ancestor
             */
            if (!eleInput.isEmpty()) {
                eleInput.get(0).sendKeys(actualText);
            } else {
                List<WebElement> eleTextarea = parent.findElements(
                        By.xpath(
                                "./ancestor::div[@class='z12JJ']/following-sibling::div[@jscontroller='oCiKKc']/descendant::textarea[@aria-label='Your answer']"));
                if (!eleTextarea.isEmpty()) {
                    long epochTime = getEpochTime();
                    eleTextarea.get(0).sendKeys(actualText + epochTime);
                } else {
                    throw new NoSuchElementException("No input or textarea found for: " + textLocator);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getEpochTime(){

        long epoch = System.currentTimeMillis() / 1000;
        return epoch;
    }


    public void radioButton(String exp){


        try {
            int experience = Integer.parseInt(exp);

            String option;

            // Step 1: Identify the appropriate range
            if (experience >= 0 && experience <= 2) {
                option = "0 - 2";
            } else if (experience >= 3 && experience <= 5) {
                option = "3 - 5";
            } else if (experience >= 6 && experience <= 10) {
                option = "6 - 10";
            } else if (experience > 10) {
                option = "> 10";
            } else {
                throw new IllegalArgumentException("Invalid experience: " + exp);
            }

            List<WebElement> eleButtons = driver
                    .findElements(By.xpath("//div[@jscontroller='sWGJ4b']/descendant::div[contains(@class,'nWQGrd')]"));

            for (WebElement button : eleButtons) {

                // String text = button.getText().trim();
                String text = button.getText();

                // if (text.equals(option))
                if (text.contains(option)) {
                    scroll(button);
                    button.click();
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void checkBox(String skils){
        
        try {
            // Step 1: Split the skills using &&
            String[] skillArray = skils.split(",");

            List<WebElement> elecheckBoxes = driver
                    .findElements(
                            By.xpath("//div[@jscontroller='sWGJ4b']/descendant::label[contains(@class,'Yri8Nb')]"));

            for (String skill : skillArray) {
                String expectedSkill = skill.trim();

                for (WebElement checkBox : elecheckBoxes) {
                    String text = checkBox.getText();
                    if (text.toLowerCase().contains(expectedSkill.toLowerCase())) {
                        scroll(checkBox);
                        checkBox.click();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void dropDown(String addressedAs) throws InterruptedException{

        try {
            WebElement eleOption = driver.findElement(By.xpath(
                    "//div[contains(@jscontroller,'sWGJ4b')]/descendant::span[contains(text(),'How should you be addressed?')]/ancestor::div[contains(@class,'z12JJ')]/following-sibling::div[1]/descendant::div[contains(@jsname,'wQNmvb')][1]"));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(eleOption));
            scroll(eleOption);
            // eleOption.click();

            JavascriptExecutor js = ((JavascriptExecutor) driver);
            js.executeScript("arguments[0].click()", eleOption);

            Thread.sleep(3000);
            List<WebElement> eleDropDown = driver
                    .findElements(By.xpath(
                            "//div[contains(@class,'ncFHed')]/descendant::span[not(contains(text(),'Choose'))]"));

            for (WebElement dropdown : eleDropDown) {
                String text = dropdown.getText();
                if (text.toLowerCase().contains(addressedAs.toLowerCase())) {
                    scroll(dropdown);
                    // dropdown.click();
                    js.executeScript("arguments[0].click()", dropdown);
                    // break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getCurrentTime(){

        // // Date and Time Both
        // LocalDateTime currentDateTime = LocalDateTime.now();

        // current Date and time
        LocalTime currentTime = LocalTime.now();

        // Convert time into String
        String formattedTime = currentTime.format(
                DateTimeFormatter.ofPattern("HH:mm"));

        return formattedTime;
        // return null;
    }


    public String getDateSevenDaysAgo(int days){
            // current Date
            LocalDate currentDate = LocalDate.now();

            // Minus 7 days
            LocalDate minusDatebydays = currentDate.minusDays(days);

            // define desired format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Format the current date
            String formattedDate = minusDatebydays.format(formatter);

            return formattedDate;
            // return null;
    }


    public void date(){

        try {
            WebElement eledate = driver.findElement(
                    By.xpath("//div[contains(@jscontroller,'sWGJ4b')]/descendant::input[contains(@type,'date')]"));
            scroll(eledate);
            eledate.sendKeys(getDateSevenDaysAgo(7));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void time(String hours, String minutes){
        
        try {
            WebElement parent = driver.findElement(By.xpath("//div[contains(@class,'ocBCTb')]"));
            scroll(parent);

            WebElement eleHours = parent.findElement(
                    By.xpath(".//descendant::input[contains(@aria-label,'Hour')]"));
            eleHours.sendKeys(hours);

            WebElement eleMinutes = parent.findElement(
                    By.xpath(".//descendant::input[contains(@aria-label,'Minute')]"));
            eleMinutes.sendKeys(minutes);

            // // Approach 1
            // WebElement eleAfterandBefoeMidDay = parent
            // .findElement(By.xpath(".//descendant::div[contains(@data-value, '" + AM_PM +
            // "')]"));

            // Approach 2
            // String xpathExpr = String.format(".//descendant::div[contains(@data-value,
            // '%s')]", AM_PM);
            // WebElement eleAfterandBefoeMidDay = parent.findElement(By.xpath(xpathExpr));
            // eleAfterandBefoeMidDay.click();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void submit(){

        try {
            WebElement eleSubmit = driver.findElement(By.xpath("//span[contains(text(),'Submit')]"));
            scroll(eleSubmit);
            eleSubmit.click();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void successfullCompletion(String response){
        try {
            WebElement eleSuccess = driver
                    .findElement(
                            By.xpath("//div[contains(@class,'idZHHb')]/descendant::div[contains(@class,'vHW8K')]"));
            String text = eleSuccess.getText();
            System.out.println(text);
            if (text.toLowerCase().contains(response.toLowerCase())) {
                System.out.println("Successfully Submitted Form");
            } else {
                System.out.println("Unsuccessfull to Submit the Form");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


}






