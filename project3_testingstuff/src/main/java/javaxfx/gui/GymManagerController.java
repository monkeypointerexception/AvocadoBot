package javaxfx.gui;

import gym.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GymManagerController {

    private ClassSchedule schedule = new ClassSchedule();
    private MemberDatabase database = new MemberDatabase();

    @FXML
    private TextField classInput, instructorInput, fnameInput, lnameInput, localeInput, dateInput;
    @FXML
    private Button checkIn, loadClasses;

    @FXML
    void checkIn(ActionEvent event) {
        if (!validateInput())
            return;
        Member member = validateMember();
        if (member == null)
            return;
        FitnessClass searchFor = new FitnessClass();
        searchFor.setClassName(classInput.getText());
        searchFor.setInstructor(instructorInput.getText());
        searchFor.setLocation(selectLocation(localeInput.getText()));
        FitnessClass classToAdd = this.schedule.searchForClass(searchFor);
        if (classToAdd == null) {
            System.out.println(searchFor.getClassName() + " by "
                    + searchFor.getInstructor()+ " does not exist at " + searchFor.getLocation().toString());
            return;
        }
        classToAdd.addMember(member);
        schedule.printClasses();
    }

    private boolean validateInput() {
        //first validate the location
        Location location = selectLocation(localeInput.getText());
        if (location == null) {
            System.out.println(localeInput.getText() + " - invalid location.");
            return false;
        }
        //instructor and class
        boolean classExists = false; boolean instructorExists = false;
        for(FitnessClass theClass: this.schedule.getClasses()) {
            if (theClass.getInstructor().equalsIgnoreCase(instructorInput.getText()))
                instructorExists = true;
            if (theClass.getClassName().equalsIgnoreCase(classInput.getText()))
                classExists = true;
            if (instructorExists && classExists)
                break;
        }
        if (!classExists) {
            System.out.println(classInput.getText() + " - class does not exist.");
            return false;
        }
        if (!instructorExists) {
            System.out.println(instructorInput.getText() + " - instructor does not exist.");
            return false;
        }
        return true;
    }

    private Member validateMember() {
        //date
        Date date = new Date(dateInput.getText());
        if(!date.isValid()) {
            System.out.println("DOB " + date.toString() + ": invalid calendar date!");
            return null;
        }
        //member in db
        Member member = new Member(fnameInput.getText(), lnameInput.getText(),
                date, null, null);
        if (!this.database.isMember(member)) {
            System.out.println(fnameInput.getText() + " " + lnameInput + " " +
                    date.toString() + " " + " is not in the database.");
            return null;
        }
        //membership expired
        Member officialMember = this.database.findPerson(member);
        if (officialMember.getExpire().dateHasPassed()) {
            System.out.println(fnameInput.getText() + " " + lnameInput + " membership expired.");
            return null;
        }
        return officialMember;
    }
    @FXML
    void loadFitnessClasses() {
        try {
            Scanner scan = new Scanner(new File("loadfiles/classSchedule.txt"));

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!line.isEmpty()) {
                    FitnessClass newClass = new FitnessClass();
                    String[] parse = line.split(" ");
                    newClass.setClassName(parse[0].toUpperCase());
                    newClass.setInstructor(parse[1].toUpperCase());
                    newClass.setTime(selectTime(parse[2]));
                    newClass.setLocation(selectLocation(parse[3]));
                    this.schedule.addClass(newClass);
                }
            }
            this.schedule.printClasses();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        historyMembers();
    }
    @FXML
    private void historyMembers() {
        try {
            Scanner file = new Scanner(new File("loadfiles/memberList.txt"));

            System.out.println("-list of members loaded-");

            while (file.hasNext()) {
                String line = file.nextLine();
                line = line.replaceAll("\\s+", " ");
                String[] fields = (line.trim()).split(" ");
                Member newMember = new Member(
                        fields[0], fields[1], new Date(fields[2]), new Date(fields[3]), fields[4]
                );
                if (this.database.add(newMember))
                    System.out.println(newMember);
            }

            System.out.println("-end of list-");
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }
    private Time selectTime(String time) {
        return switch (time) {
            case "morning" -> Time.morning;
            case "afternoon" -> Time.afternoon;
            case "evening" -> Time.evening;
            default -> null;
        };
    }

    private Location selectLocation(String location) {
        return switch (location.toLowerCase()) {
            case "bridgewater" -> Location.BRIDGEWATER;
            case "piscataway" -> Location.PISCATAWAY;
            case "somerville" -> Location.SOMERVILLE;
            case "franklin" -> Location.FRANKLIN;
            case "edison" -> Location.EDISON;
            default -> null;
        };
    }
}