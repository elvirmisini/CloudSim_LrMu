import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class LrMuSimulation {

    public static void main(String[] args) {
        Log.printLine("Starting LrMuSimulation...");

        try {
            // Step 1: Initialize CloudSim
            CloudSim.init(1, Calendar.getInstance(), false);

            // Step 2: Create a heterogeneous data center
            Datacenter datacenter = createHeterogeneousDatacenter("HeteroDatacenter");

            // Step 3: Create a broker
            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            // Step 4: Create virtual machines
            List<Vm> vmlist = createVirtualMachines(brokerId);

            // Submit VM list to the broker
            broker.submitVmList(vmlist);

            // Step 5: Create cloudlets
            List<Cloudlet> cloudletList = createCloudlets(brokerId);

            // Submit cloudlet list to the broker
            broker.submitCloudletList(cloudletList);

            // Bind cloudlets to VMs
            for (int i = 0; i < cloudletList.size(); i++) {
                broker.bindCloudletToVm(cloudletList.get(i).getCloudletId(), vmlist.get(i).getId());
            }

            // Step 6: Start the simulation
            CloudSim.startSimulation();

            // Step 7: Stop the simulation
            CloudSim.stopSimulation();

            // Step 8: Print results
            List<Cloudlet> resultList = broker.getCloudletReceivedList();
            printCloudletList(resultList);

            Log.printLine("LrMuSimulation finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Error in LrMuSimulation");
        }
    }

   private static Datacenter createHeterogeneousDatacenter(String name) {
    // Step 2: Describe the problem - Create a heterogeneous data center
    // Implement data center creation with heterogeneity

    // 1. Create a list to store hosts with different characteristics
    List<Host> hostList = new ArrayList<>();

    // 2. Create first host with higher processing power
    List<Pe> peList1 = new ArrayList<>();
    int mips1 = 2000;
    peList1.add(new Pe(0, new PeProvisionerSimple(mips1)));
    int ram1 = 8192; // 8GB
    long storage1 = 1000000; // 1TB
    int bw1 = 10000; // 10 Gbps
    hostList.add(new Host(
            0, new RamProvisionerSimple(ram1),
            new BwProvisionerSimple(bw1), storage1, peList1,
            new VmSchedulerTimeShared(peList1)));

    // 3. Create second host with moderate processing power
    List<Pe> peList2 = new ArrayList<>();
    int mips2 = 1000;
    peList2.add(new Pe(0, new PeProvisionerSimple(mips2)));
    int ram2 = 4096; // 4GB
    long storage2 = 500000; // 500GB
    int bw2 = 5000; // 5 Gbps
    hostList.add(new Host(
            1, new RamProvisionerSimple(ram2),
            new BwProvisionerSimple(bw2), storage2, peList2,
            new VmSchedulerTimeShared(peList2)));

    // 4. Create a DatacenterCharacteristics object that stores the properties of the data center
    String arch = "x86"; // system architecture
    String os = "Linux"; // operating system
    String vmm = "Xen";
    double time_zone = 10.0; // time zone
    double cost = 3.0; // cost of using processing in this resource
    double costPerMem = 0.05; // cost of using memory in this resource
    double costPerStorage = 0.001; // cost of using storage in this resource
    double costPerBw = 0.0; // cost of using bw in this resource

    // 5. Create a DatacenterCharacteristics object
    LinkedList<Storage> storageList = new LinkedList<>(); // no SAN devices for now
    DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
            arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

    // 6. Create a Datacenter with the specified characteristics
    Datacenter datacenter = null;
    try {
        datacenter = new Datacenter(
                name, characteristics,
                new VmAllocationPolicySimple(hostList),
                storageList, 0);
    } catch (Exception e) {
        e.printStackTrace();
    }

    return datacenter;
}


    // Step 3: Model the diagram of the main classes used
    // (Datacenter, Broker, VM, Cloudlet, etc.)

    // Step 4: Model the architecture of the problem (flow chart or logical architecture)
    // - Add comments throughout the code to explain the logical flow

    // Step 5: Adapt the code according to the description of the problem
    // - Ensure that LR and MU policies are appropriately implemented
    // - Add comments to explain the implementation details

    // Step 6: Testing the problem with different parameters (metrics)
    // - You can modify the parameters of VMs, cloudlets, etc., and observe the simulation results

    // Step 7: Generation (extraction) and interpretation of results
    // - The printCloudletList method can be extended to include additional metrics or details

    private static DatacenterBroker createBroker() {
        // Implement broker creation
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return broker;
    }

    private static List<Vm> createVirtualMachines(int brokerId) {
        // Implement VM creation with LR and MU policies
        List<Vm> vmlist = new ArrayList<>();

        // Your implementation of LR and MU policies here

        // Example VM creation (replace with your logic)
        Vm vm1 = new Vm(0, brokerId, 1000, 1, 512, 1000, 10000, "Xen", new CloudletSchedulerTimeShared());
        Vm vm2 = new Vm(1, brokerId, 2000, 1, 256, 1000, 20000, "Xen", new CloudletSchedulerTimeShared());

        vmlist.add(vm1);
        vmlist.add(vm2);

        return vmlist;
    }

    private static List<Cloudlet> createCloudlets(int brokerId) {
        // Implement cloudlet creation
        List<Cloudlet> cloudletList = new ArrayList<>();

        // Your implementation of cloudlet creation here

        // Example cloudlet creation (replace with your logic)
        for (int i = 0; i < 2; i++) {
            Cloudlet cloudlet = new Cloudlet(i, 400000, 1, 300, 300, new UtilizationModelFull(),
                    new UtilizationModelFull(), new UtilizationModelFull());
            cloudlet.setUserId(brokerId);
            cloudletList.add(cloudlet);
        }

        return cloudletList;
    }

    private static void printCloudletList(List<Cloudlet> list) {
        // Implement printing of cloudlet results
        for (Cloudlet cloudlet : list) {
            Log.printLine("Cloudlet ID: " + cloudlet.getCloudletId() +
                    ", Status: " + cloudlet.getCloudletStatus() +
                    ", Resource ID: " + cloudlet.getResourceId() +
                    ", VM ID: " + cloudlet.getVmId() +
                    ", Time: " + cloudlet.getActualCPUTime() +
                    ", Start Time: " + cloudlet.getExecStartTime() +
                    ", Finish Time: " + cloudlet.getFinishTime());
        }
    }
}
