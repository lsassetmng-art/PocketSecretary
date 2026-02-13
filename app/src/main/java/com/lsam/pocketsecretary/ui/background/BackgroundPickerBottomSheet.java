package com.lsam.pocketsecretary.ui.background;

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
import com.lsam.pocketsecretary.core.background.BackgroundMetaLoader;
import com.lsam.pocketsecretary.core.settings.BackgroundStore;

public class BackgroundPickerBottomSheet extends BottomSheetDialogFragment {

    private Runnable onChanged;

    public BackgroundPickerBottomSheet(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sheet_background_picker,
                container, false);

        LinearLayout containerLayout =
                view.findViewById(R.id.containerBackgrounds);

        addBackgroundOption(containerLayout, "default", "Default Desk");

        return view;
    }

    private void addBackgroundOption(LinearLayout container,
                                     String backgroundId,
                                     String label) {

        View item = LayoutInflater.from(getContext())
                .inflate(R.layout.item_background_option, container, false);

        String previewPath =
                AssetRepository.backgroundImage(backgroundId, "day");

        Bitmap bmp = AssetRepository.loadBitmap(getContext(), previewPath);

        item.findViewById(R.id.imgCheck)
                .setVisibility(
                        BackgroundStore.get(requireContext())
                                .equals(backgroundId)
                                ? View.VISIBLE
                                : View.GONE
                );

        if (bmp != null) {
            ((android.widget.ImageView)
                    item.findViewById(R.id.imgPreview))
                    .setImageBitmap(bmp);
        }

        ((android.widget.TextView)
                item.findViewById(R.id.txtLabel))
                .setText(label);

        item.setOnClickListener(v -> {

            BackgroundStore.set(requireContext(), backgroundId);

            if (onChanged != null) {
                onChanged.run();
            }

            dismiss();
        });

        container.addView(item);
    }
}
