package sample.mainController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.datamodel.ClassGroup;
import sample.datamodel.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ReadingListGeneratorUtil {

    static void readingListGenerator(ClassGroup classGroup, List<String> books) {
        for (int n = 0; n < classGroup.getStudentList().size(); n++) {
            Student tempStudent = classGroup.getStudentList().get(n);
            for (int l = 0; l < classGroup.getStudyYear().getLengthOfYear(); l++) {
                tempStudent.getBooksToRead().add(l, " ");
            }
        }
        for (int i = 0; i < classGroup.getStudentList().size(); i++) {
            boolean repeat = false;
            Student student = classGroup.getStudentList().get(i);
            ObservableList<String> readingList = FXCollections.observableArrayList();
            List<String> tempList = new ArrayList<>(books);
            for (int j = 0; j < classGroup.getStudyYear().getLengthOfYear(); j++) {
                List<String> anotherTempBookList = new ArrayList<>(tempList);
                Random random = new Random();
                int randomNumber = random.nextInt(tempList.size());
                for (int k = 0; k < classGroup.getStudentList().size(); k++) {
                    Student student1 = classGroup.getStudentList().get(k);
                    while (anotherTempBookList.get(randomNumber).equals(student1.getBooksToRead().get(j))) {
                        if (anotherTempBookList.size() == 1) {
                            repeat = true;
                            break;
                        } else {
                            anotherTempBookList.remove(randomNumber);
                            randomNumber = random.nextInt(anotherTempBookList.size());
                            k = -1;
                        }
                    }
                }
                readingList.add(j, anotherTempBookList.get(randomNumber));
                tempList.remove(anotherTempBookList.get(randomNumber));
                if (repeat) {
                    for (int l = 0; l < classGroup.getStudyYear().getLengthOfYear(); l++) {
                        readingList.add(l, " ");
                    }
                    j = -1;
                    tempList.clear();
                    tempList.addAll(books);
                    repeat = false;
                }
            }
            student.setBooksToRead(readingList);
        }
    }

    static void readingListGeneratorForSelectedStudents(ClassGroup classGroup, Student student, List<String> books) {
        ObservableList<String> readingList = FXCollections.observableArrayList();
        List<String> tempBookList = new ArrayList<>(books);
        boolean repeat = false;
        for (int i = 0; i < classGroup.getStudyYear().getLengthOfYear(); i++) {
            student.getBooksToRead().set(i, " ");
        }
        for (int i = 0; i < classGroup.getStudyYear().getLengthOfYear(); i++) {
            List<String> unavailableBooks = new ArrayList<>();
            for (int j = 0; j < classGroup.getStudentList().size(); j++) {
                Student students = classGroup.getStudentList().get(j);
                unavailableBooks.add(students.getBooksToRead().get(i));
            }
            List<String> availableBooks = new ArrayList<>(books);
            availableBooks.removeAll(unavailableBooks);
            Random random = new Random();
            int randomNumber = random.nextInt(availableBooks.size());
            while (!tempBookList.contains(availableBooks.get(randomNumber))) {
                if (availableBooks.size() == 1) {
                    repeat = true;
                    break;
                } else {
                    availableBooks.remove(randomNumber);
                    randomNumber = random.nextInt(availableBooks.size());
                }
            }
            readingList.add(i, availableBooks.get(randomNumber));
            tempBookList.remove(availableBooks.get(randomNumber));
            if (repeat) {
                i = -1;
                tempBookList.clear();
                tempBookList.addAll(books);
                repeat = false;
            }
        }
        student.setBooksToRead(readingList);
    }
}
