package com.example.Database.DAO;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Optional;

public interface DataAcccessObject<T> {
    void addListener(TableView<T> tableView);
    ObservableList<T> getAll();
    boolean add(T t);
    boolean update(int index, T t);
    boolean delete(T t);
    Optional<T> findByNo(String no);
    List<T> findContains(String keyword);
}
