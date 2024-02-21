/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import java.util.Collections;
import model.History;
import model.Worker;
import view.Validate;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import view.Menu;
/**
 *
 * @author tung
 */
public class Manager extends Menu<String> {
    private Validate validation;
    private ArrayList<Worker> lw;
    private ArrayList<History> lh;

    public Manager() {
        title = "Worker Management System";
        String[] options = {"Add Worker", "Increase salary", "Decrease salary", "Show adjusted", "Exit"};
        super.mChon = new ArrayList<>();
        for (String option : options) {
            super.mChon.add(option);
        }
        lw = new ArrayList<>();
        lh = new ArrayList<>();
        validation = new Validate();
    }

    @Override
    public void execute(int n) {
        switch (n) {
            case 1:
                addWorker();
                break;
            case 2:
                changeSalary(1);
                break;
            case 3:
                changeSalary(2);
                break;
            case 4:
                printListHistory();
                break;
            case 5:
                return;
        }
    }

    public void addWorker() {
        System.out.print("Enter code: ");
        String id = validation.checkInputString();
        if (!validation.checkIdExist(lw, id)) {
            System.err.println("Code (id) must exist in DB.");
            return;
        }
        System.out.print("Enter name: ");
        String name = validation.checkInputString();
        System.out.print("Enter age: ");
        int age = validation.checkInputIntLimit(18, 50);
        System.out.print("Enter salary: ");
        int salary = validation.checkInputSalary();
        System.out.print("Enter work location: ");
        String workLocation = validation.checkInputString();
        if (!validation.checkWorkerExist(lw, id, name, age, salary, workLocation)) {
            System.err.println("Duplicate worker.");
        } else {
            lw.add(new Worker(id, name, age, salary, workLocation));
            System.err.println("Add success.");
        }
    }

    public void changeSalary(int status) {
        if (lw.isEmpty()) {
            System.err.println("List empty.");
            return;
        }
        System.out.print("Enter code: ");
        String id = validation.checkInputString();
        Worker worker = getWorkerByCode(lw, id);
        if (worker == null) {
            System.err.println("Worker does not exist.");
        } else {
            int salaryCurrent = worker.getSalary();
            int salaryUpdate;
            if (status == 1) {
                System.out.print("Enter salary: ");
                while (true) {
                    salaryUpdate = validation.checkInputSalary();
                    if (salaryUpdate <= salaryCurrent) {
                        System.err.println("Must be greater than current salary.");
                        System.out.print("Enter again: ");
                    } else {
                        break;
                    }
                }
                lh.add(new History("UP", getCurrentDate(), worker.getId(),
                        worker.getName(), worker.getAge(), salaryUpdate,
                        worker.getWorkLocation()));
            } else {
                System.out.print("Enter salary: ");
                while (true) {
                    salaryUpdate = validation.checkInputSalary();
                    if (salaryUpdate >= salaryCurrent) {
                        System.err.println("Must be smaller than current salary.");
                        System.out.print("Enter again: ");
                    } else {
                        break;
                    }
                }
                lh.add(new History("DOWN", getCurrentDate(), worker.getId(),
                        worker.getName(), worker.getAge(), salaryUpdate,
                        worker.getWorkLocation()));
            }
            worker.setSalary(salaryUpdate);
            System.err.println("Update success");
        }
    }

    public void printListHistory() {
        if (lh.isEmpty()) {
            System.err.println("List empty.");
            return;
        }
        System.out.printf("%-5s%-15s%-5s%-10s%-10s%-20s\n", "Code", "Name", "Age",
                "Salary", "Status", "Date");
        Collections.sort(lh);

        for (History history : lh) {
            printHistory(history);
        }
    }

    public Worker getWorkerByCode(ArrayList<Worker> lw, String id) {
        for (Worker worker : lw) {
            if (id.equalsIgnoreCase(worker.getId())) {
                return worker;
            }
        }
        return null;
    }

    public String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    public void printHistory(History history) {
        System.out.printf("%-5s%-15s%-5d%-10d%-10s%-20s\n", history.getId(),
                history.getName(), history.getAge(), history.getSalary(),
                history.getStatus(), history.getDate());
    }
}