package com.project.pointofsaleproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.project.pointofsaleproject.R;
import com.project.pointofsaleproject.screen.AboutActivity;
import com.project.pointofsaleproject.screen.EditProfilActivity;

public class HomeSettingFragment extends Fragment {
    Button btnAkun, btnChat, btnTentang;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_setting, container, false);

        context = getContext();
        btnAkun = root.findViewById(R.id.btn_akun);
        btnChat = root.findViewById(R.id.btn_chat);
        btnTentang = root.findViewById(R.id.btn_about);

        btnAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditProfilActivity.class);
                startActivity(intent);
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToWhatsAppChat("6282321017426");
            }
        });

        btnTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AboutActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void redirectToWhatsAppChat(String phoneNumber) {
        String formattedNumber = phoneNumber.replace("+", "").replaceAll("\\s", ""); // Remove spaces and plus sign
        String url = "https://api.whatsapp.com/send?phone=" + formattedNumber;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}