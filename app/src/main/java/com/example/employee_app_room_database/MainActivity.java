package com.example.employee_app_room_database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.employee_app_room_database.Adapter.EmployeeAdapter;
import com.example.employee_app_room_database.BottomSheet.AddEmployee;
import com.example.employee_app_room_database.Database.DatabaseHelper;
import com.example.employee_app_room_database.Entity.Employee;
import com.example.employee_app_room_database.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    List<Employee> employeeList;
    DatabaseHelper databaseHelper;
    EmployeeAdapter employeeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        employeeList = new ArrayList<>();
        // static function call hua bina function ka object bnae
        databaseHelper = DatabaseHelper.getInstance(MainActivity.this);
        showEmployees();

        binding.addFirstEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEmployee addEmp = new AddEmployee(databaseHelper, MainActivity.this);
                addEmp.show(getSupportFragmentManager(), addEmp.getTag());
            }
        });

        binding.addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.addFirstEmployee.performClick();
            }
        });

        binding.deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.employeeDao().deleteAllEmployees();
                showEmployees();
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

    }

    private void filter(String text) {
        ArrayList<Employee> filteredlist = new ArrayList<>();

        for (Employee item : employeeList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            employeeAdapter.filterList(filteredlist);
        }
    }

    public void showEmployees() {
        if (databaseHelper.employeeDao().getAllEmployees().size() > 0) {
            employeeList = databaseHelper.employeeDao().getAllEmployees();
            employeeAdapter = new EmployeeAdapter(employeeList, MainActivity.this, databaseHelper);
            binding.recyclerViewEmployees.setAdapter(employeeAdapter);
            binding.recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            binding.nothingFoundCard.setVisibility(View.GONE);
            binding.deleteAllBtn.setVisibility(View.VISIBLE);
            binding.recyclerViewEmployees.setVisibility(View.VISIBLE);
            binding.searchView.setVisibility(View.VISIBLE);

        } else {
            binding.deleteAllBtn.setVisibility(View.GONE);
            binding.nothingFoundCard.setVisibility(View.VISIBLE);
            binding.recyclerViewEmployees.setVisibility(View.GONE);
            binding.searchView.setVisibility(View.GONE);
        }
    }
}