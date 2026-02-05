import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

public class ModbusMasterHelper {
    private ModbusMaster master;
    private final String ip;
    private final int port;
    private final int timeout = 3000;

    public ModbusMasterHelper(String ip, int port) {
        this.ip = ip;
        this.port = port;
        initMaster();
    }

    private void initMaster() {
        try {
            TcpParameters tcpParams = new TcpParameters();
            tcpParams.setHost(ip);
            tcpParams.setPort(port);
            tcpParams.setKeepAlive(true);

            master = ModbusMasterFactory.createModbusMasterTCP(tcpParams);
            master.setResponseTimeout(timeout);
        } catch (Exception e) {
            throw new RuntimeException("Modbus初始化失败", e);
        }
    }

    /**
     * 读取线圈状态
     * @param slaveId 从站地址
     * @param offset 寄存器偏移量
     * @param quantity 读取数量
     * @return 线圈状态数组
     */
    public boolean[] readCoils(int slaveId, int offset, int quantity) {
        try {
            return master.readCoils(slaveId, offset, quantity);
        } catch (Exception e) {
            throw new RuntimeException("读取线圈失败", e);
        }
    }

    /**
     * 写入单个线圈
     * @param slaveId 从站地址
     * @param offset 寄存器偏移量
     * @param value 写入值（true/false）
     */
    public void writeSingleCoil(int slaveId, int offset, boolean value) {
        try {
            master.writeSingleCoil(slaveId, offset, value);
        } catch (Exception e) {
            throw new RuntimeException("写入线圈失败", e);
        }
    }

    /**
     * 读取保持寄存器
     * @param slaveId 从站地址
     * @param offset 寄存器偏移量
     * @param quantity 读取数量
     * @return 寄存器值数组
     */
    public int[] readHoldingRegisters(int slaveId, int offset, int quantity) {
        try {
            return master.readHoldingRegisters(slaveId, offset, quantity);
        } catch (Exception e) {
            throw new RuntimeException("读取寄存器失败", e);
        }
    }

    /**
     * 写入单个寄存器
     * @param slaveId 从站地址
     * @param offset 寄存器偏移量
     * @param value 写入值
     */
    public void writeSingleRegister(int slaveId, int offset, int value) {
        try {
            master.writeSingleRegister(slaveId, offset, value);
        } catch (Exception e) {
            throw new RuntimeException("写入寄存器失败", e);
        }
    }

    public void disconnect() {
        if (master != null && master.isConnected()) {
            try {
                master.disconnect();
            } catch (Exception e) {
                // 忽略断开连接异常
            }
        }
    }
}