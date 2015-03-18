package com.ataulm.wutson.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.discover.DiscoverActivity;
import com.ataulm.wutson.settings.SettingsActivity;

public abstract class WutsonTopLevelActivity extends WutsonActivity {

    private static final int DRAWER_GRAVITY = Gravity.START;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    protected abstract NavigationDrawerItem getNavigationDrawerItem();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_top_level);
        ViewGroup container = (ViewGroup) findViewById(R.id.top_level_container_activity_layout);
        container.removeAllViews();
        getLayoutInflater().inflate(layoutResID, container);

        populateNavigationDrawer();
    }

    private void populateNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_drawer_open_content_description, R.string.nav_drawer_close_content_description);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        final NavigationDrawerView navigationDrawerView = (NavigationDrawerView) drawerLayout.findViewById(R.id.drawer_list);
        navigationDrawerView.setupDrawerWith(new NavigationDrawerView.OnNavigationClickListener() {

            @Override
            public void onNavigationClick(NavigationDrawerItem item) {
                closeDrawer();
                switch (item) {
                    case DISCOVER_SHOWS:
                        startActivity(new Intent(WutsonTopLevelActivity.this, DiscoverActivity.class));
                        break;
                    case SETTINGS:
                        startActivity(new Intent(WutsonTopLevelActivity.this, SettingsActivity.class));
                        break;
                    case HELP_FEEDBACK:
                        navigate().toWutsonGooglePlusCommunity();
                        break;
                    default:
                        onNotImplementedActionFor(item);
                }
            }

            private void onNotImplementedActionFor(NavigationDrawerItem item) {
                String title = item.getTitle();
                Jabber.toastDisplayer().display(title);
            }

        }, getNavigationDrawerItem());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        throw new IllegalArgumentException("Item id not implemented");
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(DRAWER_GRAVITY);
    }

}
