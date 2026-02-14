package com.lsam.pocketsecretary.ui.persona;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.assets.AssetRepository;
import com.lsam.pocketsecretary.core.persona.PersonaMetaLoader;
import com.lsam.pocketsecretary.core.persona.PersonaYamlLoader;
import com.lsam.pocketsecretary.core.settings.SkinStore;

import java.util.Map;

public class SkinPickerBottomSheet extends BottomSheetDialogFragment {

    private String personaId;
    private Runnable onChanged;

    public SkinPickerBottomSheet(String personaId,
                                 Runnable onChanged) {
        this.personaId = personaId;
        this.onChanged = onChanged;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.sheet_skin_picker,
                container,
                false
        );

        LinearLayout layout =
                view.findViewById(R.id.containerSkins);

        PersonaMetaLoader.PersonaMeta meta =
                PersonaMetaLoader.load(requireContext(), personaId);

        if (meta == null) return view;

        for (String skinId : meta.availableSkins) {
            addSkinOption(layout, skinId);
        }

        return view;
    }

    private void addSkinOption(LinearLayout container,
                               String skinId) {

        View item = LayoutInflater.from(getContext())
                .inflate(R.layout.item_skin_option,
                        container,
                        false);

        // 🔥 Canonical v1.1 対応
        Map<String, String> personaYaml =
                PersonaYamlLoader.load(requireContext(), personaId);

        String visualPackId = personaYaml.get("required_visual_pack_id");

        String previewPath =
                AssetRepository.visualSkinImage(
                        visualPackId,
                        skinId,
                        "character_base.png"
                );

        Bitmap bmp =
                AssetRepository.loadBitmap(getContext(), previewPath);

        if (bmp != null) {
            ((android.widget.ImageView)
                    item.findViewById(R.id.imgPreview))
                    .setImageBitmap(bmp);
        }

        ((android.widget.TextView)
                item.findViewById(R.id.txtLabel))
                .setText(skinId);

        String currentSkin =
                SkinStore.get(requireContext(),
                        personaId,
                        skinId);

        if (currentSkin.equals(skinId)) {
            item.findViewById(R.id.imgCheck)
                    .setVisibility(View.VISIBLE);
        }

        item.setOnClickListener(v -> {

            SkinStore.set(requireContext(),
                    personaId,
                    skinId);

            if (onChanged != null) {
                onChanged.run();
            }

            dismiss();
        });

        container.addView(item);
    }
}
