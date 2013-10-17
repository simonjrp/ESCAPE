package se.chalmers.dat255.group22.escape.fragments.dialogfragments;

import se.chalmers.dat255.group22.escape.R;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Class representing a date picker fragment.
 * 
 * @author Simon Persson
 */
public class CategoryCreatorFragment extends DialogFragment
		implements
			TextView.OnEditorActionListener {

	private EditText mEditText;
	private View view;

	public CategoryCreatorFragment() {
		// Empty constructor required for DialogFragment
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_new_category_fragment,
				container);
		this.view = view;
		mEditText = (EditText) view.findViewById(R.id.custom_category_edittext);

		getDialog().setTitle(getString(R.string.custom_category));

		// Show soft keyboard automatically
		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		mEditText.setOnEditorActionListener(this);

		return view;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_DONE == actionId) {
			// Return input text to activity
			EditCategoryDialogListener activity = (EditCategoryDialogListener) getActivity();
			activity.onFinishEditDialog(mEditText.getText().toString());
			this.dismiss();
			return true;
		}
		return false;
	}

	public interface EditCategoryDialogListener {
		void onFinishEditDialog(String inputText);
	}

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.task_categories);
        if(spinner.getAdapter().getCount() > 2)
            spinner.setSelection(spinner.getAdapter().getCount() - 2);
        else
            spinner.setSelection(0);

    }
}
