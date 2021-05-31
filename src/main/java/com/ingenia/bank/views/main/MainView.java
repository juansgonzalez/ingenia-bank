package com.ingenia.bank.views.main;

import java.util.Optional;

import com.ingenia.bank.backend.model.Usuario;
import com.ingenia.bank.backend.service.UsuarioService;
import com.ingenia.bank.views.cuenta.CuentasView;
import com.ingenia.bank.views.inicio.InicioView;
import com.ingenia.bank.views.movimiento.MovimientosView;
import com.ingenia.bank.views.tarjeta.TarjetasView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.router.PageTitle;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "Bank App", shortName = "Bank App", enableInstallPrompt = false)
@Theme(themeFolder = "bankapp")
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;

    private UsuarioService usuarioService;

    public MainView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        
//        layout.getThemeList().set("dark", true);
        layout.getElement().getStyle().set("backgorund-color", "#E5E5E5");
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        
        Avatar avatar = new Avatar();
        avatar.setImage("images/avatar.svg");
        avatar.setName(getFullNameCurrentUser());
        layout.add(avatar);
        layout.add(createAvatarMenu());

        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Image ingeniaImage = new Image("images/ingenia.svg", "Ingenia logo");
        Image bankImage = new Image("images/bank.svg", "Bank");
        ingeniaImage.setWidth("40%");
        bankImage.setWidth("25%");
        bankImage.getElement().getStyle().set("margin-left", "1px");
        logoLayout.add(ingeniaImage);
        logoLayout.add(bankImage);
        //logoLayout.add(new H1("Bank App"));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        return new Tab[]{createTab("Inicio", InicioView.class),
                createTab("Cuentas", CuentasView.class),
                createTab("Tarjetas", TarjetasView.class),
                createTab("Movimientos", MovimientosView.class)
        };
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    private Component createAvatarMenu() {

        HorizontalLayout hl = new HorizontalLayout();

        Icon icon = new Icon(VaadinIcon.ANGLE_DOWN);

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setOpenOnClick(true);
        contextMenu.setTarget(hl);

        contextMenu.addItem("Logout", e -> {
            contextMenu.getUI().ifPresent(ui -> ui.getPage().setLocation("/logout"));
        });

        hl.add(new Text(getFullNameCurrentUser()));
        hl.add(icon);
        hl.setPadding(true);
        hl.setAlignItems(FlexComponent.Alignment.AUTO);

        return hl;
    }

    private Optional<Usuario> getCurrentUser(){
        return usuarioService.obtenerUsuarioActualConectado();
    }

    private String getFullNameCurrentUser(){
        Optional<Usuario> usuarioActual = getCurrentUser();
        if(usuarioActual.isPresent()){
            return usuarioActual.get().getNombreCompleto();
        }else{
            return "";
        }
    }
}
