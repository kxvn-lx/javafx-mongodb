package com.example.Database.DAO;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Optional;

public interface DataAcccessObject<T> {

    /**
     * @param tableView The tableView that will listen to the data changes when operating
     *                  CRUD on MongoDB.
     */
    void addListener(TableView<T> tableView);

    /**
     * @return An ObservableList of type T with all the items from data fetched from DB.
     */
    ObservableList<T> getAll();

    /**
     * @param t The object that will be added to the DB.
     * @return boolean if successful or not.
     */
    boolean add(T t);

    /**
     * @param index The index of the object with respect to data.
     * @param t The object to be updated.
     * @return boolean if successful or not.
     */
    boolean update(int index, T t);

    /**
     * @param t The object to be deleted.
     * @return boolean if successful or not
     */
    boolean delete(T t);

    /**
     * Find a single T from data.
     * @param no The unique no id of the object.
     * @return An optional T if found or not.
     */
    Optional<T> findByNo(String no);

    /**
     * @param keyword The keyword to filter if data contains in `data` has the word.
     * @return A list of type T containg all that contains.
     */
    List<T> findContains(String keyword);
}
