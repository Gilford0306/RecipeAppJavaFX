package org.example.recipeappjavafx;

import javafx.scene.control.TextArea; // Импорт класса TextArea из пакета javafx.scene.control для создания текстового поля
import java.sql.Connection; // Импорт класса Connection из пакета java.sql для работы с соединением с базой данных
import java.sql.DriverManager; // Импорт класса DriverManager из пакета java.sql для работы с драйвером JDBC
import java.sql.PreparedStatement; // Импорт класса PreparedStatement из пакета java.sql для выполнения запросов к базе данных
import java.sql.ResultSet; // Импорт класса ResultSet из пакета java.sql для работы с результатами запросов к базе данных
import java.sql.SQLException; // Импорт класса SQLException из пакета java.sql для обработки исключений SQL
import java.util.ArrayList; // Импорт класса ArrayList из пакета java.util для создания списков
import java.util.List; // Импорт класса List из пакета java.util для работы со списками

public class RecipeDAO { // Объявление класса Recipe Data access object для работы с базой данных
    static final String DB_URL = "jdbc:mysql://localhost:3306/recipes_db";
    static final String USER = "root";
    static final String PASS = null;


    public static void addRecipe(String name, String ingredients, String instructions) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO recipes (name, ingredients, instructions) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql); // Подготовка SQL-запроса
            pstmt.setString(1, name); // Установка параметра в SQL-запросе
            pstmt.setString(2, ingredients);
            pstmt.setString(3, instructions);
            pstmt.executeUpdate(); // Выполнение SQL-запроса
            System.out.println("Рецепт успешно добавлен в базу данных.");
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении рецепта: " + e.getMessage());
        }
    }


    public static void deleteRecipe(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) { // Установление соединения с базой данных
            String sql = "DELETE FROM recipes WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Рецепт успешно удален из базы данных.");
            } else { // Если количество удаленных строк равно нулю
                System.out.println("Рецепт с указанным ID не найден.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении рецепта: " + e.getMessage()); //
        }
    }

    // Метод для отображения ингредиентов и инструкций выбранного рецепта
    public static void displayRecipeDetails(int id, TextArea ingredientsTextArea, TextArea instructionsTextArea) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT ingredients, instructions FROM recipes WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String ingredients = rs.getString("ingredients"); // Получение ингредиентов из результата выборки
                String instructions = rs.getString("instructions");
                ingredientsTextArea.setText(ingredients);
                instructionsTextArea.setText(instructions);
            } else {
                ingredientsTextArea.setText("");
                instructionsTextArea.setText("");
                System.out.println("Рецепт с указанным ID не найден.");
            }
            rs.close(); // Закрытие объекта ResultSet
            pstmt.close(); // Закрытие объекта PreparedStatement
        } catch (SQLException e) {
            System.out.println("Ошибка при получении деталей рецепта: " + e.getMessage());
        }
    }

    // Метод для получения всех названия рецептов из базы данных
    public static List<String> getAllRecipes() {
        List<String> recipes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM recipes";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                recipes.add(id + ". " + name);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Ошибка при получении всех рецептов: " + e.getMessage());
        }
        return recipes;
    }
}
