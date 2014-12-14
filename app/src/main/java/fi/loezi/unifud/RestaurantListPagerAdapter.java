package fi.loezi.unifud;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

import fi.loezi.unifud.model.Restaurant;
import fi.loezi.unifud.util.MessiApiHelper;

public class RestaurantListPagerAdapter extends FragmentStatePagerAdapter {

    final Context context;
    final SparseArray<Fragment> registeredFragments;
    final ArrayList<Restaurant> restaurants;

    public RestaurantListPagerAdapter(final Context context,
                                      final FragmentManager fragmentManager,
                                      final ArrayList<Restaurant> restaurants) {

        super(fragmentManager);

        this.context = context;
        this.restaurants = restaurants;
        registeredFragments = new SparseArray<Fragment>();
    }

    @Override
    public int getCount() {

        return MessiApiHelper.DAYS_VISIBLE;
    }

    @Override
    public Fragment getItem(final int position) {

        if (registeredFragments.get(position) == null) {
            final Fragment created = RestaurantListFragment.newInstance(position, restaurants);
            registeredFragments.put(position, created);
        }

        return registeredFragments.get(position);
    }

    @Override
    public int getItemPosition(final Object object) {

        return POSITION_NONE;
    }

    @Override
    public void destroyItem(final ViewGroup container,
                            final int position,
                            final Object object) {

        super.destroyItem(container, position, object);

        registeredFragments.remove(position);
    }
}