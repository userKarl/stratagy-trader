package com.zd.business.service.ctp;

import java.io.File;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.business.common.CommonUtil;

/**
 * @author sun0x00@gmail.com
 */
public class CtpGateway  {

	private static Logger log = LoggerFactory.getLogger(CtpGateway.class);

	protected String gatewayID;
	protected String gatewayDisplayName;
	protected String gatewayLogInfo;

	protected GatewaySetting gatewaySetting;
	
	static {
		try {
			if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {

				String envTmpDir = System.getProperty("java.io.tmpdir");
				String tempLibPath = envTmpDir + File.separator + "xyz" + File.separator + "redtorch" + File.separator + "api"
						+ File.separator + "jctp" + File.separator + "lib";
//				
//				CommonUtil.copyURLToFileForTmp(tempLibPath, CtpGateway.class.getResource("/assembly/libiconv.dll"));
//				CommonUtil.copyURLToFileForTmp(tempLibPath, CtpGateway.class.getResource("/assembly/thostmduserapi.dll"));
//				CommonUtil.copyURLToFileForTmp(tempLibPath,
//						CtpGateway.class.getResource("/assembly/jctpmdapiv6v3v11x64.dll"));
//				CommonUtil.copyURLToFileForTmp(tempLibPath, CtpGateway.class.getResource("/assembly/thosttraderapi.dll"));
//				CommonUtil.copyURLToFileForTmp(tempLibPath,
//						CtpGateway.class.getResource("/assembly/jctptraderapiv6v3v11x64.dll"));
//				
				System.load(tempLibPath + File.separator + "libiconv.dll");
				System.load(tempLibPath + File.separator + "thostmduserapi.dll");
				System.load(tempLibPath + File.separator + "jctpmdapiv6v3v11x64.dll");
				System.load(tempLibPath + File.separator + "thosttraderapi.dll");
				System.load(tempLibPath + File.separator + "jctptraderapiv6v3v11x64.dll");
			} else {

				String envTmpDir = "/tmp";
				String tempLibPath = envTmpDir + File.separator + "xyz" + File.separator + "redtorch" + File.separator + "api"
						+ File.separator + "jctp" + File.separator + "lib";
				
				CommonUtil.copyURLToFileForTmp(tempLibPath, CtpGateway.class.getResource("/assembly/libthostmduserapi.so"));
				CommonUtil.copyURLToFileForTmp(tempLibPath, CtpGateway.class.getResource("/assembly/libthosttraderapi.so"));
				CommonUtil.copyURLToFileForTmp(tempLibPath,
						CtpGateway.class.getResource("/assembly/libjctpmdapiv6v3v11x64.so"));
				CommonUtil.copyURLToFileForTmp(tempLibPath,
						CtpGateway.class.getResource("/assembly/libjctptraderapiv6v3v11x64.so"));

				System.load(tempLibPath + File.separator + "libthostmduserapi.so");
				System.load(tempLibPath + File.separator + "libjctpmdapiv6v3v11x64.so");
				System.load(tempLibPath + File.separator + "libthosttraderapi.so");
				System.load(tempLibPath + File.separator + "libjctptraderapiv6v3v11x64.so");
			}
		} catch (Exception e) {
			log.error("复制库失败!", e);
		}

	}

	private HashMap<String, String> contractExchangeMap = new HashMap<>();
	private HashMap<String, Integer> contractSizeMap = new HashMap<>();

	private MdSpi mdSpi = new MdSpi(this);

	public CtpGateway(GatewaySetting gatewaySetting) {
		this.gatewaySetting = gatewaySetting;
		this.gatewayID = gatewaySetting.getGatewayID();
		this.gatewayDisplayName = gatewaySetting.getGatewayDisplayName();
		this.gatewayLogInfo = "接口ID-[" + gatewayID + "] 名称-[" + gatewayDisplayName+"] >>> ";
	}

	public HashMap<String, String> getContractExchangeMap() {
		return contractExchangeMap;
	}

	public HashMap<String, Integer> getContractSizeMap() {
		return contractSizeMap;
	}


	public void connect() {
		if (mdSpi != null) {
			mdSpi.connect();
		}
	}



	public void setContractExchangeMap(HashMap<String, String> contractExchangeMap) {
		this.contractExchangeMap = contractExchangeMap;
	}

	public boolean isConnected() {
		return mdSpi != null && mdSpi.isConnected();
	}
	public GatewaySetting getGatewaySetting() {
		return gatewaySetting;
	}

}
