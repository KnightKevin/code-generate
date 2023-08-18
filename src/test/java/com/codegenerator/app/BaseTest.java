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
import com.vmware.vim25.TraversalSpec;
import com.vmware.vim25.VirtualDevice;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

@Slf4j
public class BaseTest {
    String host = "host246";
    String username = "administrator@vshpare.local";
    String password = "Testing%123";

    String TEXT_VIRTUAL_MACHINE = "VirtualMachine";
    String TEXT_FOLDER = "Folder";
    String TEXT_COMPUTE_RESOURCE = "ComputeResource";
    String TEXT_CLUSTER_COMPUTE_RESOURCE = "ClusterComputeResource";

    /**
     * 遍历了datacenter中所有虚机和模板
     * */
    @Test
    public void list1() throws MalformedURLException, RemoteException {

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

    /**
     * 遍历datacenter
     * */
    @Test
    public void list2() throws MalformedURLException, RemoteException {


        ServiceInstance serviceInstance = new ServiceInstance(new URL("https://" + host + "/sdk"), username, password, true);

        ManagedEntity[] mes = serviceInstance.getRootFolder().getChildEntity();
        for (ManagedEntity i : mes) {
            log.error("DataCenter: {}", i.getName());
        }


    }

    /**
     * 遍历compute_resource
     * */
    @Test
    public void list3() throws MalformedURLException, RemoteException {



        ServiceInstance serviceInstance = new ServiceInstance(new URL("https://" + host + "/sdk"), username, password, true);

        ManagedObjectReference cv = serviceInstance.getViewManager().createContainerView(serviceInstance.getRootFolder(), new String[]{TEXT_CLUSTER_COMPUTE_RESOURCE}, true).getMOR();



        PropertyFilterSpec pfs = new PropertyFilterSpec();


        TraversalSpec ts = new TraversalSpec();
        ts.setType("ContainerView");
        ts.setPath("view");
        ts.setName("traverseEntities");
        ts.setSkip(false);

        ObjectSpec os = new ObjectSpec();
        os.setObj(cv);
        os.setSkip(true);
        os.setSelectSet(new SelectionSpec[]{ts});

        PropertySpec ps = new PropertySpec();
        ps.setType(TEXT_CLUSTER_COMPUTE_RESOURCE);
        ps.setPathSet(new String[]{"name"});


        pfs.setObjectSet(new ObjectSpec[]{os});
        pfs.setPropSet(new PropertySpec[]{ps});



        PropertyFilterSpec[] pfsList = new PropertyFilterSpec[1];
        pfsList[0] = pfs;

        RetrieveResult result = serviceInstance.getPropertyCollector().retrievePropertiesEx(pfsList, new RetrieveOptions());


        for (ObjectContent i: result.getObjects()) {
            printInfo(i);
        }


    }

    /**
     * 根据id获取虚拟机信息，并执行开机操作
     * */
    @Test
    public void getVm() throws MalformedURLException, RemoteException {

        ServiceInstance serviceInstance = getServiceInstance();

        ManagedObjectReference mor = new ManagedObjectReference();
        mor.setType(TEXT_VIRTUAL_MACHINE);
        mor.setVal("vm-623");
        VirtualMachine v = new VirtualMachine(serviceInstance.getServerConnection(), mor);

        VirtualDevice[] virtualDevices = v.getConfig().getHardware().getDevice();

        for (VirtualDevice i : virtualDevices) {

            log.error("device name is {}, type is {}", i.getDeviceInfo().getLabel(), i.getDeviceInfo().getDynamicType());
        }


        log.error("vm is {}, ip is {}", v.getName(), v.getGuest().ipAddress);


//        v.powerOnVM_Task(null);


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

    private ServiceInstance getServiceInstance() throws MalformedURLException, RemoteException {
        return  new ServiceInstance(new URL("https://" + host + "/sdk"), username, password, true);
    }
}
