/* SPDX-License-Identifier: MIT */

package atlantafx.sampler.layout;

import static atlantafx.sampler.layout.MainModel.SubLayer.PAGE;
import static atlantafx.sampler.layout.MainModel.SubLayer.SOURCE_CODE;

import atlantafx.sampler.event.DefaultEventBus;
import atlantafx.sampler.event.NavEvent;
import atlantafx.sampler.page.Page;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import atlantafx.sampler.page.components.*;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

public class MainModel {



    public static final Class<? extends Page> DEFAULT_PAGE = DashboardPage.class;

    private static final Map<Class<? extends Page>, NavTree.Item> NAV_TREE = createNavItems();

    public enum SubLayer {
        PAGE,
        SOURCE_CODE
    }

    NavTree.Item getTreeItemForPage(Class<? extends Page> pageClass) {
        return NAV_TREE.getOrDefault(pageClass, NAV_TREE.get(DEFAULT_PAGE));
    }

    List<NavTree.Item> findPages(String filter) {
        return NAV_TREE.values().stream()
            .filter(item -> item.getValue() != null && item.getValue().matches(filter))
            .toList();
    }

    public MainModel() {
        DefaultEventBus.getInstance().subscribe(NavEvent.class, e -> navigate(e.getPage()));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Properties                                                            //
    ///////////////////////////////////////////////////////////////////////////

    // ~
    private final ReadOnlyObjectWrapper<Class<? extends Page>> selectedPage = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Class<? extends Page>> selectedPageProperty() {
        return selectedPage.getReadOnlyProperty();
    }

    // ~
    private final ReadOnlyObjectWrapper<SubLayer> currentSubLayer = new ReadOnlyObjectWrapper<>(PAGE);

    public ReadOnlyObjectProperty<SubLayer> currentSubLayerProperty() {
        return currentSubLayer.getReadOnlyProperty();
    }

    // ~
    private final ReadOnlyObjectWrapper<NavTree.Item> navTree = new ReadOnlyObjectWrapper<>(createTree());

    public ReadOnlyObjectProperty<NavTree.Item> navTreeProperty() {
        return navTree.getReadOnlyProperty();
    }

    private NavTree.Item createTree() {

        var dashboard = NavTree.Item.page("Dashboard", new FontIcon(Material2OutlinedMZ.MENU), DashboardPage.class);
        var wallet = NavTree.Item.page("Wallet", new FontIcon(Material2OutlinedMZ.MONEY), WalletPage.class);
        var watchlist = NavTree.Item.page("Watchlist", new FontIcon(Material2OutlinedMZ.PAGES), WatchlistPage.class);
        var events = NavTree.Item.page("Events", new FontIcon(Material2OutlinedMZ.PAGEVIEW), EventsPage.class);
        var UserEvents = NavTree.Item.page("Events", new FontIcon(Material2OutlinedMZ.PAGEVIEW), UserEventsPage.class);
        //var xchange = NavTree.Item.page("X-Change", new FontIcon(Material2OutlinedMZ.PUBLISH), XChangePage.class);
        var profile = NavTree.Item.page("Profile", new FontIcon(Material2OutlinedMZ.SETTINGS), ProfilePage.class);



        var root = NavTree.Item.root();
        root.getChildren().setAll(
            dashboard,
                wallet,
                watchlist,
                events,
                UserEvents,
                //xchange,
                profile
        );

        return root;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Nav Tree                                                              //
    ///////////////////////////////////////////////////////////////////////////

    public static Map<Class<? extends Page>, NavTree.Item> createNavItems() {
        var map = new HashMap<Class<? extends Page>, NavTree.Item>();

        // general
        map.put(DashboardPage.class, NavTree.Item.page(DashboardPage.NAME, DashboardPage.class));
        map.put(WalletPage.class, NavTree.Item.page(WalletPage.NAME, WalletPage.class));
        map.put(WatchlistPage.class, NavTree.Item.page(WatchlistPage.NAME, WatchlistPage.class));
        map.put(EventsPage.class, NavTree.Item.page(EventsPage.NAME, EventsPage.class));
        map.put(UserEventsPage.class, NavTree.Item.page(EventsPage.NAME, UserEventsPage.class));

       // map.put(XChangePage.class, NavTree.Item.page(XChangePage.NAME, XChangePage.class));
        map.put(ProfilePage.class, NavTree.Item.page(ProfilePage.NAME, ProfilePage.class));

        return map;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Commands                                                              //
    ///////////////////////////////////////////////////////////////////////////

    public void navigate(Class<? extends Page> page) {
        selectedPage.set(Objects.requireNonNull(page));
        currentSubLayer.set(PAGE);
    }

    public void showSourceCode() {
        currentSubLayer.set(SOURCE_CODE);
    }

    public void hideSourceCode() {
        currentSubLayer.set(PAGE);
    }
}
