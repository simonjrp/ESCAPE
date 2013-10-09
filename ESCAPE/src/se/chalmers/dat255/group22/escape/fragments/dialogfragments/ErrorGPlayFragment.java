package se.chalmers.dat255.group22.escape.fragments.dialogfragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * ErrorGPLayFragment is a DialogFragment for displaying error dialogs after
 * contacting Google Play services.
 * 
 * @author Simon Persson
 * 
 */
public class ErrorGPlayFragment extends DialogFragment {

	private Dialog dialog;

	/**
	 * Creates a new ErrorGPLayFragment.
	 */
	public ErrorGPlayFragment() {
		super();
		dialog = null;
	}

	/**
	 * Set the dialog to use with this dialog fragment.
	 * 
	 * @param dialog
	 *            The dialog object.
	 */
	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return dialog;
	}
}
