package de.mkrtchyan.recoverytools;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

import de.mkrtchyan.recoverytools.view.SlidingTabLayout;

//TODO: Improve and clean up...

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BackupRestoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackupRestoreFragment extends Fragment {

    private RashrActivity mActivity;
    private Context mContext;
    private Device mDevice;
    private ViewPager mPager;
    private BackupRestorePagerAdapter mAdapter;

    public BackupRestoreFragment() {
    }

    public static BackupRestoreFragment newInstance(RashrActivity activity) {
        BackupRestoreFragment fragment = new BackupRestoreFragment();
        fragment.mActivity = activity;
        return fragment;
    }

    public static void showPopup(final RashrActivity activity, final View v, final boolean isRecovery, final ArrayAdapter<String> adapter, final BackupRestorePagerAdapter pagerAdapter) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.getMenuInflater().inflate(R.menu.bakmgr_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                final CharSequence text = ((AppCompatTextView) v).getText();
                try {

                    final String FileName = text.toString();

                    final AppCompatDialog dialog = new AppCompatDialog(activity);
                    dialog.setTitle(R.string.setname);
                    dialog.setContentView(R.layout.dialog_input);
                    final AppCompatButton bGo = (AppCompatButton) dialog.findViewById(R.id.bGoBackup);
                    final AppCompatEditText etFileName = (AppCompatEditText) dialog.findViewById(R.id.etFileName);
                    final File path = isRecovery ?
                            Const.PathToRecoveryBackups : Const.PathToKernelBackups;
                    switch (menuItem.getItemId()) {
                        case R.id.iRestore:
                            FlashUtil RestoreUtil = new FlashUtil(activity, new File(path, FileName),
                                    isRecovery ? FlashUtil.JOB_RESTORE_RECOVERY : FlashUtil.JOB_RESTORE_KERNEL);
                            RestoreUtil.execute();
                            return true;
                        case R.id.iRename:
                            etFileName.setHint(FileName);
                            bGo.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    String Name;
                                    if (etFileName.getText() != null && etFileName.isEnabled()
                                            && !etFileName.getText().toString().equals("")) {
                                        Name = etFileName.getText().toString();
                                    } else {
                                        Name = String.valueOf(etFileName.getHint());
                                    }

                                    if (!Name.endsWith(activity.getDevice().getRecoveryExt())) {
                                        Name = Name + activity.getDevice().getRecoveryExt();
                                    }

                                    File renamedBackup = new File(path, Name);

                                    if (renamedBackup.exists()) {
                                        Toast
                                                .makeText(activity, R.string.backupalready, Toast.LENGTH_SHORT)
                                                .show();
                                    } else {
                                        File Backup = new File(path, FileName);
                                        if (Backup.renameTo(renamedBackup)) {
                                            loadBackups(activity.getDevice(), adapter, isRecovery);
                                        } else {
                                            Toast
                                                    .makeText(activity, R.string.rename_failed, Toast.LENGTH_SHORT)
                                                    .show();
                                        }

                                    }
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            return true;
                        case R.id.iDeleteBackup:
                            if (new File(path, text.toString()).delete()) {
                                Toast.makeText(activity, activity.getString(R.string.bak_deleted),
                                        Toast.LENGTH_SHORT).show();
                                ArrayAdapter<String> adapter;
                                if (isRecovery) {
                                    adapter = pagerAdapter.getRecoveryBackupFragment().getAdapter();
                                } else {
                                    adapter = pagerAdapter.getKernelBackupFragment().getAdapter();
                                }
                                loadBackups(activity.getDevice(), adapter, isRecovery);
                            }
                            return true;
                        default:
                            return false;
                    }
                } catch (Exception e) {
                    if (e.getMessage().contains("EINVAL") && text.toString().contains(":")) {
                        AlertDialog.Builder adialog = new AlertDialog.Builder(activity);
                        adialog.setMessage(R.string.check_name);
                        adialog.setMessage(R.string.ok);
                        adialog.show();
                    }
                    activity.addError(Const.RASHR_TAG, e, false);
                    return false;
                }
            }
        });
        popup.show();
    }

    public static void loadBackups(Device device, ArrayAdapter<String> adapter, boolean isRecovery) {
        if ((isRecovery && !(device.isRecoveryDD() || device.isRecoveryMTD()))
                || !isRecovery && !(device.isKernelDD() || device.isKernelMTD())) {
            adapter.add("Operation not supported");
        } else {
            File path = isRecovery ? Const.PathToRecoveryBackups : Const.PathToKernelBackups;
            if (path.listFiles() != null) {
                File FileList[] = path.listFiles();
                adapter.clear();
                for (File backup : FileList) {
                    if (!backup.isDirectory()) adapter.add(backup.getName());
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_backup_restore, container, false);
        mActivity = (RashrActivity) getActivity();
        mContext = root.getContext();
        mDevice = mActivity.getDevice();
        mPager = (ViewPager) root.findViewById(R.id.vpBackupRestore);
        mAdapter = new BackupRestorePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mAdapter);
        SlidingTabLayout slidingTabLayout =
                (SlidingTabLayout) root.findViewById(R.id.stlBackupRestore);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = mContext.getTheme();
                theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                return typedValue.data;
            }

            @Override
            public int getDividerColor(int position) {
                return 0;
            }
        });
        slidingTabLayout.setViewPager(mPager);
        final FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabCreateBackup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isRecovery = mPager.getCurrentItem() == 0;
                createBackup(isRecovery);
            }
        });
        fab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_add_white));
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    if (mDevice.isRecoveryDD() || mDevice.isRecoveryMTD()) {
                        fab.setVisibility(View.VISIBLE);
                    } else {
                        fab.setVisibility(View.INVISIBLE);
                    }
                } else {

                    if (mDevice.isKernelDD() || mDevice.isKernelMTD()) {
                        fab.setVisibility(View.VISIBLE);
                    } else {
                        fab.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mAdapter.getRecoveryBackupFragment().getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!(mDevice.isRecoveryDD() || mDevice.isRecoveryMTD())) {
                    Toast.makeText(mContext, "Operation not supported", Toast.LENGTH_SHORT).show();
                } else {
                    showPopup(mActivity, view, true, mAdapter.getRecoveryBackupFragment().getAdapter(), mAdapter);
                }
            }
        });
        mAdapter.getKernelBackupFragment().getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!(mDevice.isKernelDD() || mDevice.isKernelMTD())) {
                    Toast.makeText(mContext, "Operation not supported", Toast.LENGTH_SHORT).show();
                } else {
                    showPopup(mActivity, view, false, mAdapter.getRecoveryBackupFragment().getAdapter(), mAdapter);
                }
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.backup_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void createBackup(final boolean RecoveryBackup) {
        String prefix;
        String CurrentName;
        String EXT;
        if (RecoveryBackup) {
            prefix = "recovery";
            EXT = mDevice.getRecoveryExt();
            CurrentName = mDevice.getRecoveryVersion();
        } else {
            prefix = "kernel";
            EXT = mDevice.getKernelExt();
            CurrentName = mDevice.getKernelVersion();
        }
        final AppCompatDialog dialog = new AppCompatDialog(mContext);
        dialog.setTitle(R.string.setname);
        dialog.setContentView(R.layout.dialog_input);
        final AppCompatButton bGoBackup = (AppCompatButton) dialog.findViewById(R.id.bGoBackup);
        final AppCompatEditText etFileName = (AppCompatEditText) dialog.findViewById(R.id.etFileName);
        final AppCompatCheckBox optName = (AppCompatCheckBox) dialog.findViewById(R.id.cbOptInput);
        final String NameHint = prefix + "-from-" + Calendar.getInstance().get(Calendar.DATE)
                + "-" + Calendar.getInstance().get(Calendar.MONTH)
                + "-" + Calendar.getInstance().get(Calendar.YEAR)
                + "-" + Calendar.getInstance().get(Calendar.HOUR)
                + "-" + Calendar.getInstance().get(Calendar.MINUTE) + EXT;
        optName.setText(CurrentName);
        optName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFileName.setEnabled(!optName.isChecked());
            }
        });

        etFileName.setHint(NameHint);
        bGoBackup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String EXT;
                File Path;
                final int JOB;
                if (RecoveryBackup) {
                    EXT = mDevice.getRecoveryExt();
                    Path = Const.PathToRecoveryBackups;
                    JOB = FlashUtil.JOB_BACKUP_RECOVERY;
                } else {
                    EXT = mDevice.getKernelExt();
                    Path = Const.PathToKernelBackups;
                    JOB = FlashUtil.JOB_BACKUP_KERNEL;
                }

                CharSequence Name = "";
                if (optName.isChecked()) {
                    Name = optName.getText() + EXT;
                } else {
                    if (etFileName.getText() != null && !etFileName.getText().toString().equals("")) {
                        Name = etFileName.getText().toString();
                    }

                    if (Name.equals("")) {
                        Name = String.valueOf(etFileName.getHint());
                    }

                    if (!Name.toString().endsWith(EXT)) {
                        Name = Name + EXT;
                    }
                }

                final File fBACKUP = new File(Path, Name.toString());
                if (fBACKUP.exists()) {
                    Toast
                            .makeText(mActivity, R.string.backupalready, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    final FlashUtil BackupCreator = new FlashUtil(mActivity, fBACKUP, JOB);
                    BackupCreator.setOnTaskDoneListener(new FlashUtil.OnTaskDoneListener() {
                        @Override
                        public void onSuccess() {
                            ArrayAdapter<String> adapter;
                            if (RecoveryBackup) {
                                adapter = mAdapter.getRecoveryBackupFragment().getAdapter();
                            } else {
                                adapter = mAdapter.getKernelBackupFragment().getAdapter();
                            }

                            loadBackups(mActivity.getDevice(), adapter, RecoveryBackup);
                        }

                        @Override
                        public void onFail(Exception e) {

                        }
                    });
                    BackupCreator.execute();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.FlashItem:
                mActivity.switchTo(FlashFragment.newInstance(mActivity));
                break;
        }
        return false;
    }

    public static class ListFragment extends Fragment {

        private ArrayAdapter<String> mAdapter;
        private RashrActivity mActivity;
        private ListView mListView;
        private boolean isRecovery;

        public ListFragment() {
        }

        public static ListFragment newInstance(RashrActivity activity, boolean isRecovery) {
            ListFragment fragment = new ListFragment();
            fragment.mActivity = activity;
            fragment.mListView = new ListView(activity);
            fragment.mAdapter = new ArrayAdapter<>(activity, R.layout.custom_list_item);
            fragment.mListView.setAdapter(fragment.getAdapter());
            fragment.isRecovery = isRecovery;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            loadBackups(mActivity.getDevice(), mAdapter, isRecovery);
            return mListView;
        }

        public ArrayAdapter<String> getAdapter() {
            return mAdapter;
        }

        public ListView getListView() {
            return mListView;
        }

    }

    public class BackupRestorePagerAdapter extends FragmentPagerAdapter {

        private ListFragment mRecoveryBackupFragment = null;
        private ListFragment mKernelBackupFragment = null;

        public BackupRestorePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return getRecoveryBackupFragment();
                default:
                    return getKernelBackupFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Recovery Backups";
                case 1:
                    return "Kernel Backups";
            }
            return "Rashr";
        }

        public ListFragment getRecoveryBackupFragment() {
            if (mRecoveryBackupFragment != null) {
                return mRecoveryBackupFragment;
            }
            mRecoveryBackupFragment = ListFragment.newInstance(mActivity, true);
            return mRecoveryBackupFragment;
        }

        public ListFragment getKernelBackupFragment() {
            if (mKernelBackupFragment != null) {
                return mKernelBackupFragment;
            }
            mKernelBackupFragment = ListFragment.newInstance(mActivity, false);
            return mKernelBackupFragment;
        }
    }
}
