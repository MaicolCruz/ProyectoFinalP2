package co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.factory;
//import co.edu.uniquindio.proyectofinal.proyecto_finalp2.mapping.mappers.IMarketPlaceMapping;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.model.Vendedor;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.interfaces.*;
import co.edu.uniquindio.proyectofinal.proyecto_finalp2.service.impl.*;

public class ServiceFactory {
    private static ServiceFactory instance;
    private final IMarketPlaceMapping marketPlaceMapping;

    private ServiceFactory() {
            this.marketPlaceMapping = new MarketPlaceMappingImpl();
    }

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public IMarketPlaceMapping createMarketPlaceMapping() {
        return marketPlaceMapping;
    }

    public IAdministradorCrud createAdministradorCrud() {
        return new AdministradorCrudImpl();
    }

    public IVendedorCrud createVendedorCrud(Vendedor vendedor) {
        return new VendedorCrudImpl(vendedor);
    }


}