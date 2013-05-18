package de.mkrtchyan.recoverytools;

import java.io.File;



public class Support {
	
	public String DEVICE = android.os.Build.DEVICE;
	public String BOARD = android.os.Build.BOARD;
	public String RecoveryPath;
	public String SYSTEM;
	public String VERSION;
	public String EXT = ".img";
	public String HOST_URL = "http://dslnexus.nazuka.net/recoveries";
	public boolean KINDLE = false;
	public boolean KERNEL_TO = false;
	public boolean FLASH_OVER_RECOVERY = false;
	public boolean MTD = false;
	public boolean BLM = false;
	public boolean TWRP = true;
	public boolean CWM = true;
	public boolean TWRP_OFFICIAL = true;
	public boolean CWM_OFFICIAL = true;
	

	public Support(){
		
//	Set DEVICE predefined options
		
//		Asus Transformer Infinity
		if (DEVICE.equals("tf700t"))
			EXT = ".blob";
		
//		Kindle Fire HD 7"
		if (DEVICE.equals("D01E"))
			DEVICE = "kfhd7";
		
//		Galaxy Note 
		if (DEVICE.equals("GT-N7000")
				|| DEVICE.equals("n7000")
				|| DEVICE.equals("galaxynote")
				|| DEVICE.equals("N7000")
				|| BOARD.equals("GT-N7000")
				|| BOARD.equals("n7000")
				|| BOARD.equals("galaxynote")
				|| BOARD.equals("N7000")){
			DEVICE = "n7000";
			FLASH_OVER_RECOVERY = true;
			EXT = ".zip";
		}
		
//		Galaxy Note 2
		if (DEVICE.equals("t03g")
				|| DEVICE.equals("n7100")
				|| DEVICE.equals("GT-N7100")
				|| BOARD.equals("t03g")
				|| BOARD.equals("n7100")
				|| BOARD.equals("GT-N7100"))
			DEVICE = "n7100";
//		Galaxy Note 2 LTE
		if (DEVICE.equals("t0ltexx")
				|| DEVICE.equals("GT-N7105")
				|| DEVICE.equals("t0ltedv")
				|| DEVICE.equals("GT-N7105T")
				|| DEVICE.equals("t0lteatt")
				|| DEVICE.equals("SGH-I317")
				|| DEVICE.equals("t0ltetmo")
				|| DEVICE.equals("SGH-T889")
				|| DEVICE.equals("t0ltecan")
				|| DEVICE.equals("t0ltevl")
				|| DEVICE.equals("SGH-I317M")
				|| BOARD.equals("t0ltexx")
				|| BOARD.equals("GT-N7105")
				|| BOARD.equals("t0ltedv")
				|| BOARD.equals("GT-N7105T")
				|| BOARD.equals("t0lteatt")
				|| BOARD.equals("SGH-I317")
				|| BOARD.equals("t0ltetmo")
				|| BOARD.equals("SGH-T889")
				|| BOARD.equals("t0ltecan")
				|| BOARD.equals("t0ltevl")
				|| BOARD.equals("SGH-I317M"))
			DEVICE = "t0lte";
			
//		Galaxy S3 (international)
		if (DEVICE.equals("GT-I9300")
				|| DEVICE.equals("Galaxy S3")
				|| DEVICE.equals("GalaxyS3")
				|| DEVICE.equals("m0")
				|| DEVICE.equals("i9300")
				|| BOARD.equals("GT-I9300")
				|| BOARD.equals("m0")
				|| BOARD.equals("i9300"))
			DEVICE = "i9300";
		
//		Galaxy S2
		if (DEVICE.equals("GT-I9100G")
				|| DEVICE.equals("GT-I9100M")
				|| DEVICE.equals("GT-I9100P")
				|| DEVICE.equals("GT-I9100")
				|| DEVICE.equals("galaxys2")
				|| BOARD.equals("GT-I9100G")
				|| BOARD.equals("GT-I9100M")
				|| BOARD.equals("GT-I9100P")
				|| BOARD.equals("GT-I9100")
				|| BOARD.equals("galaxys2")){
			FLASH_OVER_RECOVERY = true;
			EXT = ".zip";
			DEVICE = "galaxys2";
		}
		
//		Galaxy S2 ATT
		if (DEVICE.equals("SGH-I777")
				|| DEVICE.equals("galaxys2att")
				|| BOARD.equals("SGH-777")
				|| BOARD.equals("galaxys2att")){
			DEVICE = "galaxys2att";
			FLASH_OVER_RECOVERY = true;
			EXT = ".zip";
		}
		
//		Galaxy S2 LTE (skyrocket)
		if (DEVICE.equals("SGH-I727")
				|| BOARD.equals("skyrocket")
				|| BOARD.equals("SGH-I727"))
			DEVICE = "skyrocket";
		
//		Sony Xperia Z C6603
		
		if (DEVICE.equals("C6603"))
			EXT = ".tar";
			
//		Sony Xperia S
		
		if (DEVICE.equals("LT26i"))
			DEVICE = "nozomi";
		
//		Sony Xperia T
		
		if (DEVICE.equals("LT30p"))
			DEVICE = "mint";
		
//		MTD Devices
		if (DEVICE.equals("crespo")
				|| DEVICE.equals("crespo4g")
				|| DEVICE.equals("passion")
				|| DEVICE.equals("saga")
				|| DEVICE.equals("swift")
				|| DEVICE.equals("geeb"))
			MTD = true;
		
		if (BOARD.equals("ace"))
			DEVICE = "ace";
		
//		Devices who kernel will be flashed to
		if (DEVICE.equals("nozomi")
				|| DEVICE.equals("mint"))
			KERNEL_TO = true;
		
		
		RecoveryPath = getRecoveryPath();
		getSupportedSystems();
	}
	
	private String getRecoveryPath(){
		
		String tmp = "";
		
//		Nexus DEVICEs + Same
		
		if (DEVICE.equals("maguro")
				|| DEVICE.equals("toro")
				|| DEVICE.equals("toroplus"))
			tmp = "/dev/block/platform/omap/omap_hsmmc.0/by-name/recovery";
		
		if (DEVICE.equals("grouper") 
				|| DEVICE.equals("endeavoru") 
				|| DEVICE.equals("tilapia")) 
			tmp = "/dev/block/platform/sdhci-tegra.3/by-name/SOS";
		
		if (DEVICE.equals("mako"))
			tmp = "/dev/block/platform/msm_sdcc.1/by-name/recovery";
		
		if (DEVICE.equals("manta"))
			tmp = "/dev/block/platform/dw_mmc.0/by-name/recovery";
		
//		Samsung DEVICEs + Same
		
		if (DEVICE.equals("d2att")
				|| DEVICE.equals("d2tmo")
				|| DEVICE.equals("d2vzw"))
			tmp = "/dev/block/mmcblk0p18";
		
		if (DEVICE.equals("i9300")
				|| DEVICE.equals("galaxys2"))
			tmp = "/dev/block/mmcblk0p6";
		
		if (DEVICE.equals("n7100")
				|| DEVICE.equals("tf700t")
				|| DEVICE.equals("t0ltevzw"))
//				|| DEVICE.equals("m3"))
			tmp = "/dev/block/mmcblk0p9";
		
		if (DEVICE.equals("golden") 
				|| DEVICE.equals("villec2")) 
			tmp = "/dev/block/mmcblk0p21";
		
		if (DEVICE.equals("n7000"))
			tmp = "/dev/block/platform/dw_mmc/by-name/RECOVERY";
		
		if (DEVICE.equals("jena"))
			tmp = "/dev/block/mmcblk0p12";
		
//		HTC DEVICEs + Same
		
		if (DEVICE.equals("ace") 
				|| DEVICE.equals("primou")) 
			tmp = "/dev/block/platform/msm_sdcc.2/mmcblk0p21";
		
		if (DEVICE.equals("pyramid"))
			tmp = "/dev/block/platform/msm_sdcc.1/mmcblk0p21";
		
		if (DEVICE.equals("kingdom"))
			tmp = "/dev/block/mmcblk0p21";
		
		if (DEVICE.equals("ville")
				|| DEVICE.equals("evita")
				|| DEVICE.equals("skyrocket")
				|| DEVICE.equals("fireball"))
			tmp = "/dev/block/mmcblk0p22";
		
		if (DEVICE.equals("dlxub1"))
			tmp = "/dev/block/mmcblk0p20";
		
//		Motorola DEVICEs + Same
		
		if (DEVICE.equals("droid2"))
			tmp = "/dev/block/mmcblk1p16";
		
//		Sony DEVICEs + Same
		
		if (DEVICE.equals("nozomi")
				|| DEVICE.equals("LT26i")
				|| DEVICE.equals("mint")
				|| DEVICE.equals("LT30p"))
			tmp = "/dev/block/mmcblk0p11";
		
		if (DEVICE.equals("C6603"))
			tmp = "/system/bin/recovery.tar";
		
//		LG DEVICEs + Same
		
		if (DEVICE.equals("x3"))
			tmp = "/dev/block/mmcblk0p1";
	
//		ZTE DEVICEs + Same
		
		if (DEVICE.equals("warp2"))
			tmp = "/dev/block/mmcblk0p13";
		
		return tmp;
	}
	
	public void getSupportedSystems(){
		
		if (DEVICE.equals("galaxys2")
				|| DEVICE.equals("SGH-I777")
				|| DEVICE.equals("n7000")
				|| DEVICE.equals("x3")
				|| DEVICE.equals("droid2")
				|| DEVICE.equals("kingdom")) 
			TWRP = false;
		
		if (DEVICE.equals("nozomi")
				|| DEVICE.equals("mint")
				|| DEVICE.equals("LT30p")
				|| DEVICE.equals("kfhd7")
				|| DEVICE.equals("LT26i"))
			CWM = false;
			
		if (DEVICE.equals("")
				|| !MTD 
				&& !BLM 
				&& RecoveryPath.equals("")) {
			TWRP = false;
			CWM = false;
		}
		
	}
	
	public void getVersion(String SYSTEM) {
		
		this.SYSTEM = SYSTEM;
		
		if (SYSTEM.equals("clockwork")) {
			
//		CLOCKWORKMOD touch supported devices
			if (DEVICE.equals("ace")
					|| DEVICE.equals("crespo")
					|| DEVICE.equals("crespo4g")
					|| DEVICE.equals("d2att")
					|| DEVICE.equals("d2tmo")
					|| DEVICE.equals("endeavoru")
					|| DEVICE.equals("evita")
					|| DEVICE.equals("fireball")
					|| DEVICE.equals("galaxys2")
					|| DEVICE.equals("grouper")
					|| DEVICE.equals("i9300")
					|| DEVICE.equals("maguro")
					|| DEVICE.equals("mako")
					|| DEVICE.equals("manta")
					|| DEVICE.equals("n7100")
					|| DEVICE.equals("pyramid")
					|| DEVICE.equals("saga")
					|| DEVICE.equals("skyrocket")
					|| DEVICE.equals("t0lte")
					|| DEVICE.equals("tilapia")
					|| DEVICE.equals("toro")
					|| DEVICE.equals("toroplus")
					|| DEVICE.equals("ville")
					|| DEVICE.equals("warp2"))
				VERSION = "-touch";
			
//			Newest Clockworkmod version for devices
			
			if (DEVICE.equals("droid2"))
				VERSION = VERSION + "-5.0.2.0";
			
			if (DEVICE.equals("pyramid"))
				VERSION = VERSION + "-5.8.0.9";
			
			if (DEVICE.equals("ace")
					|| DEVICE.equals("saga")
					|| DEVICE.equals("galaxys2"))
				VERSION = VERSION + "-5.8.1.5";
			
			if (DEVICE.equals("endeavoru"))
				VERSION = VERSION + "-5.8.4.0";
			
			if (DEVICE.equals("primou"))
				VERSION = VERSION + "-5.8.4.5";
			
			if (DEVICE.equals("n7000"))
				VERSION = VERSION + "-6.0.1.2";
			
			if (DEVICE.equals("golden")
					|| DEVICE.equals("warp2"))
				VERSION = VERSION + "-6.0.2.7";
			
			if (DEVICE.equals("crespo")
					|| DEVICE.equals("crespo4g")
					|| DEVICE.equals("d2att")
					|| DEVICE.equals("d2tmo")
					|| DEVICE.equals("dlxub1")
					|| DEVICE.equals("evita")
					|| DEVICE.equals("fireball")
					|| DEVICE.equals("grouper")
					|| DEVICE.equals("i9300")
					|| DEVICE.equals("maguro")
					|| DEVICE.equals("mako")
					|| DEVICE.equals("manta")
					|| DEVICE.equals("n7100")
					|| DEVICE.equals("skyrocket")
					|| DEVICE.equals("t0lte")
					|| DEVICE.equals("tilapia")
					|| DEVICE.equals("toro")
					|| DEVICE.equals("toroplus")
					|| DEVICE.equals("ville"))
				VERSION = VERSION + "-6.0.3.1";
			
			if (VERSION.equals(""))
				CWM_OFFICIAL = false;
			
//			HOST_URL = "http://download2.clockworkmod.com/recoveries";
		}
		
		if (SYSTEM.equals("twrp")) {
			
			if (DEVICE.equals("ace")
					|| DEVICE.equals("crespo")
					|| DEVICE.equals("crespo4g")
					|| DEVICE.equals("d2att")
					|| DEVICE.equals("d2tmo")
					|| DEVICE.equals("d2vzw")
					|| DEVICE.equals("endeavoru")
					|| DEVICE.equals("evita")
					|| DEVICE.equals("fireball")
					|| DEVICE.equals("geeb")
					|| DEVICE.equals("golden")
					|| DEVICE.equals("grouper")
					|| DEVICE.equals("i9300")
					|| DEVICE.equals("maguro")
					|| DEVICE.equals("mako")
					|| DEVICE.equals("manta")
					|| DEVICE.equals("mint")
					|| DEVICE.equals("nozomi")
					|| DEVICE.equals("passion")
					|| DEVICE.equals("primou")
					|| DEVICE.equals("pyramid")
					|| DEVICE.equals("skyrocket")
					|| DEVICE.equals("t0lte")
					|| DEVICE.equals("t0lteatt")
					|| DEVICE.equals("t0ltetmo")
					|| DEVICE.equals("t0ltevzw")
					|| DEVICE.equals("tilapia")
					|| DEVICE.equals("toro")
					|| DEVICE.equals("toroplus")
					|| DEVICE.equals("ville")
					|| DEVICE.equals("villec2"))
				VERSION = "-2.5.0.0";
			
			if (DEVICE.equals("dlxub1")
					|| DEVICE.equals("n7100")
					|| DEVICE.equals("saga"))
				VERSION = "-2.5.0.1";
			
			if (VERSION.equals(""))
				TWRP_OFFICIAL = false;
			
//			HOST_URL = "http://techerrata.com/get/twrp2/" + DEVICE;
		}
	}
	
	public File constructFile(File Path) {
		
		File file = null;
		if (SYSTEM.equals("clockwork"))
			if (CWM_OFFICIAL) {
				file = new File(Path, "recovery-" + SYSTEM + VERSION + "-" + DEVICE + EXT);
			} else {
				file = new File(Path, DEVICE + "-cwm" +  EXT);
			}
			
		if (SYSTEM.equals("twrp")) 
			if (TWRP_OFFICIAL) {
				file = new File(Path, "openrecovery-" + SYSTEM + VERSION + "-" + DEVICE + EXT);
			} else {
				file = new File(Path, DEVICE + "-twrp" +  EXT);
			}
			
		return file;
	}
}