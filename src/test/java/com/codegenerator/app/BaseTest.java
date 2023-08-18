package com.codegenerator.app;

import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.RetrieveOptions;
import com.vmware.vim25.RetrieveResult;
import com.vmware.vim25.SelectionSpec;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.TraversalSpec;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.mo.ContainerView;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BaseTest {

    /**
     * 遍历了datacenter中所有虚机和模板
     * */
    @Test
    public void update() throws MalformedURLException, RemoteException {

        String host = "host246";
        String username = "administrator@vshpare.local";
        String password = "Testing%123";

        ServiceInstance serviceInstance = new ServiceInstance(new URL("https://" + host + "/sdk"), username, password, true);


        String[] vmList = new String[1];
        vmList[0] ="VirtualMachine";



        ManagedObjectReference cViewRef = serviceInstance
                .getViewManager()
                .createContainerView(serviceInstance.getRootFolder(), vmList, true)
                .getMOR();


        ObjectSpec objectSpec = new ObjectSpec();
        objectSpec.setObj(cViewRef);
        objectSpec.setSkip(true);

        TraversalSpec traversalSpec = new TraversalSpec();
        traversalSpec.setName("traverseEntities");
        traversalSpec.setPath("view");
        traversalSpec.setSkip(false);
        traversalSpec.setType("ContainerView");

        objectSpec.setSelectSet(new SelectionSpec[]{traversalSpec});

        PropertyFilterSpec[] propertyFilterSpecs = new PropertyFilterSpec[1];

        PropertyFilterSpec fSpec = new PropertyFilterSpec();
        fSpec.setObjectSet(new ObjectSpec[]{objectSpec});

        PropertySpec vmSpec = new PropertySpec();
        vmSpec.setType("VirtualMachine");
        vmSpec.setPathSet(new String[]{"name"});
        fSpec.setPropSet(new PropertySpec[]{vmSpec});

        propertyFilterSpecs[0] = fSpec;

        RetrieveOptions retrieveOptions = new RetrieveOptions();
        retrieveOptions.setMaxObjects(1000);
        RetrieveResult props = serviceInstance.getPropertyCollector().retrievePropertiesEx(propertyFilterSpecs, retrieveOptions);

        for (ObjectContent oc : props.getObjects()) {
            printInfo(oc);
        }

    }

    private static void printInfo(ObjectContent objectContent) {
        // This is super generic here... To actually relate the objects so you
        // know which HostSystem a VirtualMachine lives on you need to implement
        // some kind of inventory system and use the MOR from the HostSystem
        // and the MOR from the vm.runtime.host
        for(DynamicProperty props: objectContent.getPropSet()) {
            System.out.println(props.val);
        }
    }
}
