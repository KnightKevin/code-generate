package com.codegenerator.app;

import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.RetrieveOptions;
import com.vmware.vim25.RetrieveResult;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

@Slf4j
public class BaseTest {

    @Test
    public void update() throws MalformedURLException, RemoteException {

        String host = "host246";
        String username = "administrator@vshpare.local";
        String password = "Testing%123";

        ServiceInstance serviceInstance = new ServiceInstance(new URL("https://" + host + "/sdk"), username, password, true);

        ManagedEntity clusterMe = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntity("ClusterComputeResource", "246集群-1");
        RetrieveOptions options = new RetrieveOptions();
        options.setMaxObjects(100);
        String[] vmProps = new String[2];
        vmProps[0] = "name";
        vmProps[1] = "runtime.host";
        PropertySpec vmSpec = new PropertySpec();
        vmSpec.setAll(false);
        vmSpec.setType("VirtualMachine");
        vmSpec.setPathSet(vmProps);

        String[] hostProps = new String[4];
        hostProps[0] = "name";
        hostProps[1] = "summary.hardware.numCpuCores";
        hostProps[2] = "summary.hardware.cpuModel";
        hostProps[3] = "summary.hardware.memorySize";
        PropertySpec hostSpec = new PropertySpec();
        hostSpec.setAll(false);
        hostSpec.setType("HostSystem");
        hostSpec.setPathSet(hostProps);

        String[] clusterProps = new String[2];
        clusterProps[0] = "name";
        clusterProps[1] = "parent";
        PropertySpec clusterSpec = new PropertySpec();
        clusterSpec.setAll(false);
        clusterSpec.setType("ClusterComputeResource");
        clusterSpec.setPathSet(clusterProps);

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(clusterMe.getMOR());
        oSpec.setSelectSet(com.vmware.vim25.mo.util.PropertyCollectorUtil.buildFullTraversalV4());
        PropertyFilterSpec[] pfSpec = new PropertyFilterSpec[1];
        pfSpec[0] = new PropertyFilterSpec();

        ObjectSpec[] oo = new ObjectSpec[1];
        oo[0] = oSpec;

        pfSpec[0].setObjectSet(oo);
        PropertySpec[] pp = new PropertySpec[3];
        pp[0] = vmSpec;
        pp[1] = hostSpec;
        pp[2] = clusterSpec;

        pfSpec[0].setPropSet(pp);
        RetrieveResult ret = serviceInstance.getPropertyCollector().retrievePropertiesEx(pfSpec, options);

        for (ObjectContent aRet : ret.getObjects()) {
            if(aRet.getObj().type.equalsIgnoreCase("ClusterComputeResource")) {
                printInfo(aRet);
            }
            if(aRet.getObj().type.equalsIgnoreCase("HostSystem")) {
                System.out.println("Host Info: ");
                printInfo(aRet);
                System.out.println("#######################");
            }
            if(aRet.getObj().type.equalsIgnoreCase("VirtualMachine")) {
                System.out.println("VirtualMachine: ");
                printInfo(aRet);
                System.out.println("#######################################");
            }
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
