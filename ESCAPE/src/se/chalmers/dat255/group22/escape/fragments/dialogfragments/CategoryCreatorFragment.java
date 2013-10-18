package se.chalmers.dat255.group22.escape.fragments.dialogfragments;

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

import se.chalmers.dat255.group22.escape.R;

/**
 * Class representing a fragment where the user can create a new category.
 * 
 * @author tholene
 */
public class CategoryCreatorFragment extends DialogFragment
		implements
			TextView.OnEditorActionListener {

	private EditText categoryInput;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_new_category_fragment,
				container);
		categoryInput = (EditText) view
				.findViewById(R.id.custom_category_edittext);

		getDialog().setTitle(getString(R.string.custom_category));

		// Show soft keyboard automatically
		categoryInput.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		categoryInput.setOnEditorActionListener(this);

		return view;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_DONE == actionId) {
			// Return input text to activity
			EditCategoryDialogListener activity = (EditCategoryDialogListener) getActivity();
			activity.onFinishEditDialog(categoryInput.getText().toString());
			this.dismiss();
			return true;
		}
		return false;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		Spinner spinner = (Spinner) getActivity().findViewById(
				R.id.task_categories);
		if (spinner.getAdapter().getCount() > 2)
			spinner.setSelection(spinner.getAdapter().getCount() - 2);
		else
			spinner.setSelection(0);

	}

	/**
	 * Interface that the activity using this class will have to implement in
	 * order to do something with the data from the EditText.
	 */
	public interface EditCategoryDialogListener {
		void onFinishEditDialog(String inputText);
	}
}
