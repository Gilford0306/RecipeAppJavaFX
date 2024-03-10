package org.example.recipeappjavafx;

import javafx.application.Application; // Импорт класса Application из пакета javafx.application
import javafx.geometry.Insets; // Импорт класса Insets из пакета javafx.geometry для управления отступами
import javafx.scene.Scene; // Импорт класса Scene из пакета javafx.scene для создания сцены
import javafx.scene.control.Button; // Импорт класса Button из пакета javafx.scene.control для создания кнопок
import javafx.scene.control.Label; // Импорт класса Label из пакета javafx.scene.control для создания меток
import javafx.scene.control.ListView; // Импорт класса ListView из пакета javafx.scene.control для создания списка
import javafx.scene.control.TextArea; // Импорт класса TextArea из пакета javafx.scene.control для создания текстового поля
import javafx.scene.control.TextField; // Импорт класса TextField из пакета javafx.scene.control для создания поля ввода текста
import javafx.scene.layout.BorderPane; // Импорт класса BorderPane из пакета javafx.scene.layout для создания макета границы
import javafx.scene.layout.GridPane; // Импорт класса GridPane из пакета javafx.scene.layout для создания сетки
import javafx.scene.layout.HBox; // Импорт класса HBox из пакета javafx.scene.layout для создания горизонтального контейнера
import javafx.stage.Stage; // Импорт класса Stage из пакета javafx.stage для управления окном приложения

import java.util.List; // Импорт класса List из пакета java.util для работы со списками

public class RecipeAppJavaFX extends Application { // Объявление класса RecipeAppJavaFX, который наследуется от класса Application
    // Главный метод, который запускает приложение
    public static void main(String[] args) {
        launch(args); // Запускает приложение JavaFX
    }

    // Переопределение метода start для создания GUI
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Приложение с рецептами"); // Устанавливает заголовок окна

        // Создание компонентов GUI
        ListView<String> recipeList = new ListView<>(); // Создание списка для отображения рецептов
        TextArea ingredientsTextArea = new TextArea();
        TextArea instructionsTextArea = new TextArea();
        Label nameLabel = new Label("Название:");
        TextField nameField = new TextField(); // Создание поля для ввода названия рецепта
        Label ingredientsLabel = new Label("Ингредиенты:");
        Label instructionsLabel = new Label("Инструкции:");
        Button addButton = new Button("Добавить");
        Button deleteButton = new Button("Удалить");

        // Создание контейнеров для компонентов
        GridPane recipeDetailsPane = new GridPane(); // GridPane размещает компоненты в виде сетки с ячейками, которые выровнены по строкам и столбцам.
        recipeDetailsPane.setPadding(new Insets(10)); // Установка внутренних отступов
        recipeDetailsPane.setVgap(8); // Установка вертикального расстояния между ячейками
        recipeDetailsPane.setHgap(10); // Установка горизонтального расстояния между ячейками
        BorderPane root = new BorderPane(); // BorderPane размещает компоненты вокруг краев своего контейнера: верхнего, нижнего, левого, правого и центрального.
        root.setPadding(new Insets(10)); // Установка внутренних отступов для корневого контейнера

        // Установка компонентов на сетку для деталей рецепта
        GridPane.setConstraints(nameLabel, 0, 0); // Установка метки "Название" на сетку
        GridPane.setConstraints(nameField, 1, 0); // Установка поля ввода названия рецепта на сетку
        GridPane.setConstraints(ingredientsLabel, 0, 1);
        GridPane.setConstraints(ingredientsTextArea, 1, 1);
        GridPane.setConstraints(instructionsLabel, 0, 2);
        GridPane.setConstraints(instructionsTextArea, 1, 2);
        GridPane.setConstraints(addButton, 0, 3);
        GridPane.setConstraints(deleteButton, 1, 3);

        // Добавление компонентов на сетку для деталей рецепта
        recipeDetailsPane.getChildren().addAll(nameLabel, nameField, ingredientsLabel, ingredientsTextArea,
                instructionsLabel, instructionsTextArea);

        HBox buttonBox = new HBox(10); // Создание горизонтального контейнера для кнопок
        buttonBox.getChildren().addAll(addButton, deleteButton); // Добавление кнопок в контейнер
        recipeDetailsPane.add(buttonBox, 1, 3); // Установка контейнера для кнопок на сетку

        root.setLeft(recipeList); // Установка списка рецептов на левую сторону корневого контейнера
        root.setCenter(recipeDetailsPane); // Установка сетки деталей рецепта на центр корневого контейнера

        // Обработка выбора рецепта из списка
        recipeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { // Проверка, выбран ли какой-то элемент
                String[] parts = newValue.split("\\. "); // Разделение строки по точке и пробелу
                int selectedRecipeId = Integer.parseInt(parts[0]); // Получение идентификатора выбранного рецепта
                RecipeDAO.displayRecipeDetails(selectedRecipeId, ingredientsTextArea, instructionsTextArea); // Отображение деталей рецепта
                nameField.setText(parts[1]); // Установка названия рецепта в поле ввода
            }
        });

        // Обработка нажатия кнопки "Добавить"
        addButton.setOnAction(e -> {
            String name = nameField.getText(); // Получение названия рецепта из поля ввода
            String ingredients = ingredientsTextArea.getText();
            String instructions = instructionsTextArea.getText();
            RecipeDAO.addRecipe(name, ingredients, instructions); // Добавление рецепта в базу данных
            updateRecipeList(recipeList); // Обновление списка рецептов
            nameField.clear();
            ingredientsTextArea.clear();
            instructionsTextArea.clear();
        });

        // Обработка нажатия кнопки "Удалить"
        deleteButton.setOnAction(e -> {
            String selectedRecipe = recipeList.getSelectionModel().getSelectedItem(); // Получение выбранного рецепта
            if (selectedRecipe != null) {
                String[] parts = selectedRecipe.split("\\. "); // Разделение строки по точке и пробелу
                int id = Integer.parseInt(parts[0]); // Получение идентификатора выбранного рецепта
                RecipeDAO.deleteRecipe(id); // Удаление рецепта из базы данных
                updateRecipeList(recipeList); // Обновление списка рецептов
                ingredientsTextArea.clear();
                instructionsTextArea.clear();
            }
        });


        Scene scene = new Scene(root, 600, 400); // Создание сцены с указанными размерами
        primaryStage.setScene(scene); // Установка сцены на основной Stage


        primaryStage.show(); // Отображение окна приложения


        updateRecipeList(recipeList); // Вызов метода для обновления списка рецептов
    }

    // Метод для обновления списка рецептов
    private void updateRecipeList(ListView<String> recipeList) { // Определение метода для обновления списка рецептов
        List<String> recipes = RecipeDAO.getAllRecipes(); // Получение списка всех рецептов из базы данных
        recipeList.getItems().clear(); // Очистка списка рецептов
        recipeList.getItems().addAll(recipes); // Добавление всех рецептов в список
    }
}
