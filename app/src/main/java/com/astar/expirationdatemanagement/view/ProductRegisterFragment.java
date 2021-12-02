package com.astar.expirationdatemanagement.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.astar.expirationdatemanagement.DBInfo;
import com.astar.expirationdatemanagement.MainActivity;
import com.astar.expirationdatemanagement.dao.ProductDao;
import com.astar.expirationdatemanagement.databinding.FragmentProductRegisterBinding;
import com.astar.expirationdatemanagement.model.Product;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ProductRegisterFragment extends Fragment {

    MainActivity mainActivity;
    FragmentProductRegisterBinding binding;
    ProductDao productDao = DBInfo.appDatabase.productDao();
    public Uri productImageUri;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof MainActivity)
            mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductRegisterBinding.inflate(inflater, container, false);
        binding.btProductRegisterCaptureBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeLauncher.launch(new ScanOptions());
            }

            private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
                    result -> {
                        if (result.getContents() != null) {
                            binding.etProductRegisterProductBarcode.setText(result.getContents());
                        }
                    });
        });
        binding.btImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                galleryLauncher.launch(intent);
            }

            private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        if(result.getData() != null) {
                            binding.ivProductImagePreview.setImageURI(result.getData().getData());
                            productImageUri = result.getData().getData();
                        }
                    }
                }
            });
        });
        binding.btProductRegisterSubmit.setOnClickListener(v -> {
            String productName = binding.etProductName.getText().toString();
            String productBarcode = binding.etProductRegisterProductBarcode.getText().toString();
            if(productName.isEmpty()) {
                Toast.makeText(getContext(), "상품 이름을 적어주세요", Toast.LENGTH_SHORT).show();
                return;
            } else if(productBarcode.isEmpty()) {
                Toast.makeText(getContext(), "바코드 번호를 적어주세요", Toast.LENGTH_SHORT).show();
                return;
            } else if(productDao.getProductByBarcode(productBarcode) != null) {
                Toast.makeText(getContext(), "이미 등록된 바코드 번호입니다", Toast.LENGTH_SHORT).show();
                return;
            }
            String imageFilePath = mainActivity.saveImageFile(productImageUri, productBarcode);
            productDao.insert(new Product(productName, productBarcode, imageFilePath));
            mainActivity.removeFragment();
            mainActivity.refreshProductList();
        });
        return binding.getRoot();
    }
}