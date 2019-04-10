package sample.mainController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import sample.datamodel.ClassGroup;
import sample.datamodel.Data;
import sample.datamodel.Student;


public class Controller {

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TableView<Student> studentListTableView;
    @FXML
    private TableColumn<Student, String> booksToReadListTableColumn;
    @FXML
    private ListView<ClassGroup> classListListView;
    private Data data = Data.getInstance();
    private ClassGroup newClass = new ClassGroup("Class Group", 2019, 3, 28, 6);

    public void initialize() {
        data.getAllSchools().add(newClass);

        classListListView.getSelectionModel().selectedItemProperty().addListener((changed, oldVal, newVal) ->
        {
            ClassGroup selectedClassGroup = classListListView.getSelectionModel().getSelectedItem();
            if (selectedClassGroup != null) {
                studentListTableView.setItems(selectedClassGroup.getStudentList());
                booksToReadListTableColumn.getColumns().setAll(selectedClassGroup.getBooksToRead().getColumns());
            }
        });

        classListListView.setItems(data.getAllSchools());
        classListListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        classListListView.getSelectionModel().selectFirst();
        createFakeData();
    }

    @FXML
    public void showNewStudentDialog() {
        ClassGroup selectedClass = classListListView.getSelectionModel().getSelectedItem();
        StudentController.showNewStudentDialog(mainBorderPane, selectedClass);
    }

    @FXML
    private void showEditStudentDialog() {
        Student selectedStudent = studentListTableView.getSelectionModel().getSelectedItem();
        StudentController.showEditStudentDialog(mainBorderPane, selectedStudent);
        studentListTableView.refresh();
    }

    @FXML
    private void removeStudentFromClass() {
        ClassGroup selectedClass = classListListView.getSelectionModel().getSelectedItem();
        Student selectedStudent = studentListTableView.getSelectionModel().getSelectedItem();
        StudentController.removeStudentFromClass(selectedClass, selectedStudent);
    }

    @FXML
    private void showCreateNewClassDialog() {
        ClassGroup newClassGroup = ClassGroupController.showCreateNewClassDialog(mainBorderPane);
        if (newClassGroup != null) {
            data.getAllSchools().add(newClassGroup);
            classListListView.getSelectionModel().select(newClassGroup);
        }
    }

    @FXML
    private void showEditClassDialog() {
        ClassGroup selectedClassGroup = classListListView.getSelectionModel().getSelectedItem();
        ClassGroup newClassGroup = ClassGroupController.showEditClassDialog(mainBorderPane, selectedClassGroup);
        if (newClassGroup != null) {
            data.getAllSchools().remove(selectedClassGroup);
            data.getAllSchools().add(newClassGroup);
            classListListView.getSelectionModel().select(newClassGroup);
            classListListView.refresh();
        }
    }
    
    @FXML
    public void deleteClassDialog() {
        ClassGroup selectedClassGroup = classListListView.getSelectionModel().getSelectedItem();
        ClassGroup deletedClass = ClassGroupController.deleteClassDialog(selectedClassGroup);
        if (deletedClass != null) {
            data.getAllSchools().remove(deletedClass);
        }
    }

     @FXML
    public void distributeBooks() {
        ClassGroup selectedClassGroup = classListListView.getSelectionModel().getSelectedItem();
        if ((selectedClassGroup.getStudentList().size() <= createBookLibrary().size()) &&
                (selectedClassGroup.getStudyYear().getLengthOfYear() <= createBookLibrary().size())) {
            ReadingListGeneratorUtil.readingListGenerator(selectedClassGroup, createBookLibrary());
            studentListTableView.refresh();
        } else {
            Alert alert = AlertAndFXMLLoaderClass.alertTypeError("Error distributing books", null,
                    "You don't have enough books in the library for the selected class.");
            alert.showAndWait();
        }
    }


    @FXML
    public void readingListGeneratorForSelectedStudent() {
        ClassGroup selectedClassGroup = classListListView.getSelectionModel().getSelectedItem();
        Student selectedStudent = studentListTableView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            Alert alert = AlertAndFXMLLoaderClass.alertTypeInformation("No student selected",
                    null, "Please select the student.");
            alert.showAndWait();
            return;
        }
        ReadingListGeneratorUtil.readingListGeneratorForSelectedStudents(selectedClassGroup
                , selectedStudent, createBookLibrary());
        studentListTableView.refresh();
    }

    @FXML
    public void printReadingList() {
        ClassGroup selectedClassGroup = classListListView.getSelectionModel().getSelectedItem();
        for (Student student : selectedClassGroup.getStudentList()) {
            System.out.println(student.getFullName());
            for (String book : student.getBooksToRead()) {
                System.out.println(book);
            }
        }
    }

    private ObservableList<String> createBookLibrary() {
        ObservableList<String> bookLibrary = FXCollections.observableArrayList();
        bookLibrary.add("book 1");
        bookLibrary.add("book 2");
        bookLibrary.add("book 3");
        bookLibrary.add("book 4");
        bookLibrary.add("book 5");
        bookLibrary.add("book 6");
        return bookLibrary;
    }


    private void createFakeData() {
        Student student = new Student("John", "Doe", 2013, 1, 14);
        Student student1 = new Student("Jane", "Doe", 2013, 2, 23);
        Student student2 = new Student("James", "Doe", 2013, 3, 5);
        Student student3 = new Student("Jennifer", "Doe", 2013, 3, 5);
        Student student4 = new Student("Jeremy", "Doe", 2013, 3, 5);
        Student student5 = new Student("Jenny", "Doe", 2013, 3, 5);
        newClass.getStudentList().add(student);
        newClass.getStudentList().add(student1);
        newClass.getStudentList().add(student2);
        newClass.getStudentList().add(student3);
        newClass.getStudentList().add(student4);
        newClass.getStudentList().add(student5);

        ObservableList<String> bookLibrary = createBookLibrary();
        ReadingListGeneratorUtil.readingListGenerator(newClass, bookLibrary);
        studentListTableView.setItems(newClass.getStudentList());
    }
}
