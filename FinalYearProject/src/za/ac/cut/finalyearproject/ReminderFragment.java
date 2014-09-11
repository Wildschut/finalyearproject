package za.ac.cut.finalyearproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import za.ac.cut.finalyearproject.reminder.ReminderAlarm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReminderFragment extends Fragment {

	private static int mYear;
	private static int mMonth;
	private static int mDay;
	private static int mHour;
	private static int mMinute;
	static final int DATE_DIALOG_ID = 1;
	static final int TIME_DIALOG_ID = 0;
	private Calendar c;
	private EditText nameEdit;
	private EditText descEdit;
	private Context mContext;
	private static boolean dateFlag = false;
	private static boolean timeFlag = false;
	private String time;
	private String contentTitle;
	private String contentText;
	public static int notificationCount;
	public Button dateButton;
	public Button timeButton;
	public Button reminButton;
	public TextListener textListener;

	public ReminderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.fragment_reminder, container,
				false);

		textListener = new TextListener();
		dateButton = (Button) rootView.findViewById(R.id.dateButton);
		timeButton = (Button) rootView.findViewById(R.id.timeButton);
		reminButton = (Button) rootView.findViewById(R.id.reminderButton);
		dateButton.setEnabled(false);
		timeButton.setEnabled(false);
		reminButton.setEnabled(false);
		nameEdit = (EditText) rootView.findViewById(R.id.nameEditText);
		nameEdit.addTextChangedListener(textListener);
		descEdit = (EditText) rootView.findViewById(R.id.DescEditText);
		descEdit.addTextChangedListener(textListener);
		c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();

	}

	public class TextListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (descEdit.getText().length() == 0
					| nameEdit.getText().length() == 0) {
				dateButton.setEnabled(false);
				timeButton.setEnabled(false);
				reminButton.setEnabled(false);
			} else if (descEdit.getText().length() > 0
					& nameEdit.getText().length() > 0) {
				dateButton.setEnabled(true);
				timeButton.setEnabled(true);
				reminButton.setEnabled(true);
			}
		}
	}

	public void onReminderClicked(View view) {
		if (dateFlag & timeFlag == true) {
			notificationCount = notificationCount + 1;
			dateFlag = false;
			timeFlag = false;
			time = mYear + "-" + mMonth + "-" + mDay + " " + mHour + "-"
					+ mMinute;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh-mm");
			Date dt = null;
			try {
				dt = df.parse(time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long when = dt.getTime();
			contentTitle = nameEdit.getText().toString();
			contentText = descEdit.getText().toString();
			AlarmManager mgr = (AlarmManager) mContext
					.getSystemService(Context.ALARM_SERVICE);
			Intent notificationIntent = new Intent(mContext,
					ReminderAlarm.class);
			notificationIntent.putExtra("Name", contentTitle);
			notificationIntent.putExtra("Description", contentText);
			notificationIntent.putExtra("NotifyCount", notificationCount);
			PendingIntent pi = PendingIntent.getBroadcast(mContext,
					notificationCount, notificationIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			mgr.set(AlarmManager.RTC_WAKEUP, when, pi);
			Toast.makeText(mContext, "Your Reminder Activated",
					Toast.LENGTH_LONG).show();
			contentTitle = "";
			contentText = "";
			descEdit.setText("");
			nameEdit.setText("");
		} else if (dateFlag == false | timeFlag == false) {
			Toast.makeText(mContext, "Please choose Date & Time",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onTimeClicked(View view) {
		try {
			DialogFragment newFragment = new TimePickerFragment();
			newFragment.show(getActivity().getFragmentManager(), "timePicker");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onDateClicked(View view) {
		try {
			DialogFragment newFragment = new DatePickerFragment();
			newFragment.show(getFragmentManager(), "datePicker");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Override
	// protected Dialog onCreateDialog(int id) {
	// // TODO Auto-generated method stub
	// switch (id) {
	// case TIME_DIALOG_ID:
	// return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
	// false);
	// case DATE_DIALOG_ID:
	// return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
	// mDay);
	// }
	// return super.onCreateDialog(id);
	// }

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			dateFlag = true;
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			timeFlag = true;
		}
	};

	// protected Dialog createDialog(int id){
	// switch (id) {
	// case TIME_DIALOG_ID:
	// return new TimePickerDialog(mContext, mTimeSetListener, mHour, mMinute,
	// false);
	// case DATE_DIALOG_ID:
	// return new DatePickerDialog(mContext, mDateSetListener, mYear, mMonth,
	// mDay);
	// }
	// }

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			timeFlag = true;
		}
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			mYear = year;
			mMonth = month + 1;
			mDay = day;
			dateFlag = true;
		}
	}
}
