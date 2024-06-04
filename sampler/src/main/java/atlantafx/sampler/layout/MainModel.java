/* SPDX-License-Identifier: MIT */

package atlantafx.sampler.layout;

import static atlantafx.sampler.layout.MainModel.SubLayer.PAGE;
import static atlantafx.sampler.layout.MainModel.SubLayer.SOURCE_CODE;

import atlantafx.sampler.event.DefaultEventBus;
import atlantafx.sampler.event.NavEvent;
import atlantafx.sampler.page.Page;
import atlantafx.sampler.page.components.AccordionPage;
import atlantafx.sampler.page.components.BreadcrumbsPage;
import atlantafx.sampler.page.components.ButtonPage;
import atlantafx.sampler.page.components.CalendarPage;
import atlantafx.sampler.page.components.CardPage;
import atlantafx.sampler.page.components.ChartPage;
import atlantafx.sampler.page.components.CheckBoxPage;
import atlantafx.sampler.page.components.ChoiceBoxPage;
import atlantafx.sampler.page.components.ColorPickerPage;
import atlantafx.sampler.page.components.ComboBoxPage;
import atlantafx.sampler.page.components.ContextMenuPage;
import atlantafx.sampler.page.components.CustomTextFieldPage;
import atlantafx.sampler.page.components.DatePickerPage;
import atlantafx.sampler.page.components.DeckPanePage;
import atlantafx.sampler.page.components.DialogPage;
import atlantafx.sampler.page.components.HtmlEditorPage;
import atlantafx.sampler.page.components.InputGroupPage;
import atlantafx.sampler.page.components.ListViewPage;
import atlantafx.sampler.page.components.MenuBarPage;
import atlantafx.sampler.page.components.MenuButtonPage;
import atlantafx.sampler.page.components.MessagePage;
import atlantafx.sampler.page.components.ModalPanePage;
import atlantafx.sampler.page.components.NotificationPage;
import atlantafx.sampler.page.components.PaginationPage;
import atlantafx.sampler.page.components.PopoverPage;
import atlantafx.sampler.page.components.ProgressIndicatorPage;
import atlantafx.sampler.page.components.RadioButtonPage;
import atlantafx.sampler.page.components.ScrollPanePage;
import atlantafx.sampler.page.components.SeparatorPage;
import atlantafx.sampler.page.components.SliderPage;
import atlantafx.sampler.page.components.SpinnerPage;
import atlantafx.sampler.page.components.SplitPanePage;
import atlantafx.sampler.page.components.TabPanePage;
import atlantafx.sampler.page.components.TableViewPage;
import atlantafx.sampler.page.components.TextAreaPage;
import atlantafx.sampler.page.components.TextFieldPage;
import atlantafx.sampler.page.components.TilePage;
import atlantafx.sampler.page.components.TitledPanePage;
import atlantafx.sampler.page.components.ToggleButtonPage;
import atlantafx.sampler.page.components.ToggleSwitchPage;
import atlantafx.sampler.page.components.ToolBarPage;
import atlantafx.sampler.page.components.TooltipPage;
import atlantafx.sampler.page.components.TreeTableViewPage;
import atlantafx.sampler.page.components.TreeViewPage;
import atlantafx.sampler.page.general.*;
import atlantafx.sampler.page.showcase.BlueprintsPage;
import atlantafx.sampler.page.showcase.OverviewPage;
import atlantafx.sampler.page.showcase.filemanager.FileManagerPage;
import atlantafx.sampler.page.showcase.musicplayer.MusicPlayerPage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
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

        var dashboard = NavTree.Item.page("Dashboard", new FontIcon(Material2OutlinedMZ.SPEED), DashboardPage.class);



        var root = NavTree.Item.root();
        root.getChildren().setAll(
            dashboard
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
