package cn.ufec.meituan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import cn.ufec.meituan.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // // 由于使用了 FragmentContainerView 则要通过这种方式找到控制器 或者在 onStart 中获取控制器
        // NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(mBinding.navHostFragment.getId());
        // assert navHostFragment != null;
        // NavController navController = navHostFragment.getNavController();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 获取 Navigation 控制器
        // mController = Navigation.findNavController(this, mBinding.navHostFragment.getId());
        // mAppBarConfiguration = new AppBarConfiguration.Builder(mController.getGraph()).build();
        // NavigationUI.setupActionBarWithNavController(this, mController, mAppBarConfiguration);
    }
}