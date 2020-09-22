package sample;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javax.swing.JFileChooser;
import javax.swing.*;
import java.io.File;
import java.util.Random;

public class Controller {
    // Список переменных
    static File file;
    static double[] input_array = new double[256*256];
    public static NeuralNet neuralnet = new NeuralNet(256*256, 8);
    // Создание вектора изображения для передачи в нейронную сеть
    public static Vector train_vector;
    @FXML
    private ImageView image_view;
    @FXML
    private ComboBox flowers_box;
    @FXML
    private TextArea text_area;
    @FXML
    private Button train_button;
    @FXML
    private Button test_button;

    // Метод обработки нажатия на кнопку выбора изображения:
    public void image_choose_button_clicked() {
        JFileChooser f_chooser = new JFileChooser("C:\\Users\\MSI\\Desktop\\lab5rbf\\UnnamedFlowers");
        f_chooser.setDialogTitle("Выберите изображение цветка из папки");
        int return_value = f_chooser.showOpenDialog(null);
        if (return_value == JFileChooser.APPROVE_OPTION) {
            file = new File(f_chooser.getSelectedFile().getAbsolutePath());
            if (get_Extension(file).equals("png") || get_Extension(file).equals("jpg") || get_Extension(file).equals("jpeg")) {
                //Image image = new Image(file.toURI().toString());
                //image_view.setImage(image);
                Image scaled_image = new Image(file.toURI().toString(), 256, 256, false, false);
                image_view.setImage(scaled_image);
                input_array = image_Converter(scaled_image);
                train_button.setDisable(false);
                test_button.setDisable(false);
                // file_Renamer();
            }
            else {
                JOptionPane.showMessageDialog(null, "Вы выбрали файл неверного формата", "Сообщение", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Метод обработки нажатия на кнопку начала обучения
    public void train_button_clicked(MouseEvent mouseEvent) {
        double[] out = new double[26]; // Получение предполагаемого выходного значения
        out[flowers_box.getSelectionModel().getSelectedIndex()] = 1.0; // Установление выходного значения
        Vector train_vector = new Vector(input_array, out, file.getName()); // Получение вектора для тренировки
        Vector[] train_set = new Vector[1];
        train_set[0] = train_vector; // Помещение вектора для тренировки в тренировочный сет
        neuralnet.delta_single_train(train_set);
    }

    // Метод обработки нажатия на кнопку обучения на трейнсете
    public void train_button_auto_clicked(MouseEvent mouseEvent) {
        trainset_Learning();
    }

    // Метод обработки нажатия на кнопку задания вопроса
    public void test_button_clicked(MouseEvent mouseEvent) {
        double[] answer = neuralnet.single_test(input_array);
        int index = answer_decode(answer);
        text_area.setText("Я думаю, что это " + flowers_box.getItems().get(index) + ".");
    }

    // Метод декодирования выходного сигнала
    public int answer_decode(double[] answer) {
        for (int i = 0; i < answer.length; i++) { if (answer[i] == 1.0) { return i; } }
        return 1;
    }

    // Метод определения расширения файла
    public static String get_Extension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    // Метод сжатия изображения
    private static double[] image_Converter(Image image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                input_array[y*256 + x] = image.getPixelReader().getArgb(x, y) / -1000000;
            }
        }
        return input_array;
    }

    // Метод обучения трэйнсетами
    public static void trainset_Learning() {
        File directory = new File("C:\\Users\\MSI\\Desktop\\lab5rbf\\TrainsetFlowers");
        File[] list = directory.listFiles();
        Vector trainset[] = new Vector[list.length];
        // Формирование трейнсета
        for (int i = 0; i < list.length; i++) {
            Image scaled_image = new Image(list[i].toURI().toString(), 256, 256, false, false);
            double[] current_input = image_Converter(scaled_image);
            int output_index = output_Detector(list[i]);
            double[] current_output =  new double[8];
            current_output[output_index] = 1.0;
            Vector current_vector = new Vector(current_input, current_output, list[i].getName());
            trainset[i] = current_vector;
        }
        // Обучение на трейнсете n раз по m эпох
        for (int i = 0; i < 40; i++) {
            trainset = mix_Set(trainset);
            neuralnet.delta_single_train(trainset);
        }

    }

    // Метод определения выходных значений
    private static int output_Detector(File file) {
        String file_name = file.getName();
        if (file_name.contains("a")) return 0;
        else if (file_name.contains("b")) return 1;
        else if (file_name.contains("i")) return 2;
        else if (file_name.contains("l")) return 3;
        else if (file_name.contains("o")) return 4;
        else if (file_name.contains("r")) return 5;
        else if (file_name.contains("t")) return 6;
        else if (file_name.contains("h")) return 7;
        else return -1;
    }

    // Метод перемешивания трэйнсета
    private static Vector[] mix_Set(Vector[] array) {
        Random rnd = new Random();
        for (int i = 1; i < array.length; i++) {
            int j = rnd.nextInt(i);
            Vector temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return array;
    }

    // Метод переименования файлов в UnnamedFlowers
    private static void file_Renamer() {
        File directory = new File("C:\\Users\\MSI\\Desktop\\lab5rbf\\UnnamedFlowers");
        File[] list = directory.listFiles();
        for (int i = 0; i < list.length; i++) {
            File new_name = new File("C:\\Users\\MSI\\Desktop\\lab5rbf\\UnnamedFlowers\\" + i + "." + get_Extension(list[i]));
            list[i].renameTo(new_name);
        }
    }
}
