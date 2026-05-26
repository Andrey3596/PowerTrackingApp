package com.example.myapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import com.example.myapp.model.*;

import com.example.myapp.service.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "calories.db";
    private static final int DATABASE_VERSION = 3;

    // Конструктор
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание всех таблиц (скопировано из вашего метода createTables)
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "gender TEXT NOT NULL," +
                "weight REAL NOT NULL," +
                "height REAL NOT NULL," +
                "age INTEGER NOT NULL," +
                "active REAL NOT NULL);";

        String productsTable = "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE NOT NULL," +
                "calories_per_100g REAL NOT NULL," +
                "protein_per_100g REAL NOT NULL," +
                "fat_per_100g REAL NOT NULL," +
                "carb_per_100g REAL NOT NULL);";

        String mealsTable = "CREATE TABLE IF NOT EXISTS meals (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT NOT NULL," +
                "date TEXT NOT NULL);";

        String mealProductsTable = "CREATE TABLE IF NOT EXISTS meal_products (" +
                "meal_id INTEGER NOT NULL," +
                "product_id INTEGER NOT NULL," +
                "weight_grams REAL NOT NULL," +
                "actual_calories REAL NOT NULL," +
                "actual_protein REAL NOT NULL," +
                "actual_fat REAL NOT NULL," +
                "actual_carb REAL NOT NULL," +
                "FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE," +
                "FOREIGN KEY (product_id) REFERENCES products(id));";

        String goalsTable = "CREATE TABLE IF NOT EXISTS user_goals (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "calories REAL," +
                "protein REAL," +
                "fat REAL," +
                "carb REAL);";

        db.execSQL(usersTable);
        db.execSQL(productsTable);
        db.execSQL(mealsTable);
        db.execSQL(mealProductsTable);
        db.execSQL(goalsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Удаляем все таблицы, если они существуют
//        db.execSQL("DROP TABLE IF EXISTS users");
//        db.execSQL("DROP TABLE IF EXISTS products");
//        db.execSQL("DROP TABLE IF EXISTS meals");
//        db.execSQL("DROP TABLE IF EXISTS meal_products");
//        db.execSQL("DROP TABLE IF EXISTS user_goals");
//        // Создаём таблицы заново
//        onCreate(db);
    }

    // ---------- Методы для User ----------
    public boolean hasUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM users WHERE id = 1", null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public User loadUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, gender, weight, height, age, active FROM users WHERE id = 1", null);
        User user = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            String gender = cursor.getString(1);
            double weight = cursor.getDouble(2);
            double height = cursor.getDouble(3);
            int age = cursor.getInt(4);
            double active = cursor.getDouble(5);
            user = new User(age, weight, height, gender, name, active);
        }
        cursor.close();
        db.close();
        return user;
    }

    public void saveUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("name", user.getUsername());
        values.put("gender", user.getGender());
        values.put("weight", user.getWeight());
        values.put("height", user.getHeight());
        values.put("age", user.getAge());
        values.put("active", user.getActive());
        db.insertWithOnConflict("users", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    // ---------- Методы для Product ----------
    public void saveProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Проверка существования продукта по имени
        Cursor cursor = db.rawQuery("SELECT id FROM products WHERE name = ?", new String[]{product.getNameProduct()});
        if (cursor.moveToFirst()) {
            int existingId = cursor.getInt(0);
            product.setId(existingId);
            cursor.close();
            db.close();
            return;
        }
        cursor.close();

        // Вставка нового продукта
        ContentValues values = new ContentValues();
        values.put("name", product.getNameProduct());
        values.put("calories_per_100g", product.getCalory());
        values.put("protein_per_100g", product.getProtein());
        values.put("fat_per_100g", product.getFat());
        values.put("carb_per_100g", product.getCarb());

        long newId = db.insert("products", null, values);
        product.setId((int) newId);
        db.close();
    }

    public List<Product> loadAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, calories_per_100g, protein_per_100g, fat_per_100g, carb_per_100g FROM products", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double calories = cursor.getDouble(2);
                double protein = cursor.getDouble(3);
                double fat = cursor.getDouble(4);
                double carb = cursor.getDouble(5);
                Product p = new Product(id, name, calories, protein, fat, carb);
                products.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }
    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getNameProduct());
        values.put("calories_per_100g", product.getCalory());
        values.put("protein_per_100g", product.getProtein());
        values.put("fat_per_100g", product.getFat());
        values.put("carb_per_100g", product.getCarb());
        db.update("products", values, "id = ?", new String[]{String.valueOf(product.getId())});
        db.close();
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("products", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    // ---------- Методы для Meal ----------
    public void saveMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Вставка в таблицу meals
            ContentValues mealValues = new ContentValues();
            mealValues.put("type", meal.getTypeMeal());
            mealValues.put("date", meal.getDate().toString());
            long mealId = db.insert("meals", null, mealValues);
            if (mealId == -1) throw new RuntimeException("Не удалось вставить meal");
            meal.setId((int) mealId);

            // Вставка порций
            for (ConsumedProduct cp : meal.getConsumedProducts()) {
                ContentValues portionValues = new ContentValues();
                portionValues.put("meal_id", mealId);
                portionValues.put("product_id", cp.getProduct().getId());
                portionValues.put("weight_grams", cp.getWeightGrams());
                portionValues.put("actual_calories", cp.getCalories());
                portionValues.put("actual_protein", cp.getProtein());
                portionValues.put("actual_fat", cp.getFat());
                portionValues.put("actual_carb", cp.getCarb());
                db.insert("meal_products", null, portionValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Meal> loadAllMeals() {
        List<Meal> meals = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mealCursor = db.rawQuery("SELECT id, type, date FROM meals ORDER BY date", null);
        if (mealCursor.moveToFirst()) {
            do {
                int mealId = mealCursor.getInt(0);
                String type = mealCursor.getString(1);
                LocalDate date = LocalDate.parse(mealCursor.getString(2));

                // Загружаем продукты для этого приёма
                String sqlPortions = "SELECT mp.weight_grams, p.id, p.name, p.calories_per_100g, p.protein_per_100g, p.fat_per_100g, p.carb_per_100g " +
                        "FROM meal_products mp JOIN products p ON mp.product_id = p.id WHERE mp.meal_id = ?";
                Cursor portionCursor = db.rawQuery(sqlPortions, new String[]{String.valueOf(mealId)});
                List<ConsumedProduct> consumed = new ArrayList<>();
                if (portionCursor.moveToFirst()) {
                    do {
                        double weight = portionCursor.getDouble(0);
                        int prodId = portionCursor.getInt(1);
                        String prodName = portionCursor.getString(2);
                        double calories = portionCursor.getDouble(3);
                        double protein = portionCursor.getDouble(4);
                        double fat = portionCursor.getDouble(5);
                        double carb = portionCursor.getDouble(6);
                        Product product = new Product(prodId, prodName, calories, protein, fat, carb);
                        consumed.add(new ConsumedProduct(product, weight));
                    } while (portionCursor.moveToNext());
                }
                portionCursor.close();

                Meal meal = new Meal(consumed, type, date);
                meal.setId(mealId);
                meals.add(meal);
            } while (mealCursor.moveToNext());
        }
        mealCursor.close();
        db.close();
        return meals;
    }

    public void deleteMeal(int mealId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("meals", "id = ?", new String[]{String.valueOf(mealId)});
        db.close();
    }

    public void updateMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Обновляем запись meals
            ContentValues mealValues = new ContentValues();
            mealValues.put("type", meal.getTypeMeal());
            mealValues.put("date", meal.getDate().toString());
            db.update("meals", mealValues, "id = ?", new String[]{String.valueOf(meal.getId())});

            // Удаляем старые порции
            db.delete("meal_products", "meal_id = ?", new String[]{String.valueOf(meal.getId())});

            // Вставляем новые
            for (ConsumedProduct cp : meal.getConsumedProducts()) {
                Product product = cp.getProduct();
                if (product.getId() <= 0) {
                    saveProduct(product); // если продукт новый, получит id
                }
                ContentValues portionValues = new ContentValues();
                portionValues.put("meal_id", meal.getId());
                portionValues.put("product_id", product.getId());
                portionValues.put("weight_grams", cp.getWeightGrams());
                portionValues.put("actual_calories", cp.getCalories());
                portionValues.put("actual_protein", cp.getProtein());
                portionValues.put("actual_fat", cp.getFat());
                portionValues.put("actual_carb", cp.getCarb());
                db.insert("meal_products", null, portionValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // ---------- Методы для Goal ----------
    public void saveGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Проверка дубликата по имени
        Cursor cursor = db.rawQuery("SELECT id FROM user_goals WHERE name = ?", new String[]{goal.getName()});
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return; // цель с таким именем уже существует
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("name", goal.getName());
        values.put("calories", goal.getCalories());
        values.put("protein", goal.getProtein());
        values.put("fat", goal.getFat());
        values.put("carb", goal.getCarb());
        long id = db.insert("user_goals", null, values);
        goal.setId((int) id);
        db.close();
    }

    public List<Goal> loadAllGoals() {
        List<Goal> goals = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, calories, protein, fat, carb FROM user_goals", null);
        if (cursor.moveToFirst()) {
            do {
                Goal g = new Goal();
                g.setId(cursor.getInt(0));
                g.setName(cursor.getString(1));
                g.setCalories((Double) (cursor.isNull(2) ? null : cursor.getDouble(2)));
                g.setProtein((Double) (cursor.isNull(3) ? null : cursor.getDouble(3)));
                g.setFat((Double) (cursor.isNull(4) ? null : cursor.getDouble(4)));
                g.setCarb((Double) (cursor.isNull(5) ? null : cursor.getDouble(5)));
                goals.add(g);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return goals;
    }

    public void deleteGoal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("user_goals", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", goal.getName());
        values.put("calories", goal.getCalories());
        values.put("protein", goal.getProtein());
        values.put("fat", goal.getFat());
        values.put("carb", goal.getCarb());
        db.update("user_goals", values, "name = ?", new String[]{goal.getName()});
        db.close();
    }
}