package com.twinkle.utils;

import static com.twinkle.utils.Constant.API_KEY;
import static com.twinkle.utils.Constant.APP_ID;
import static com.twinkle.utils.Constant.SECRET_KEY;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.baidu.aip.ocr.AipOcr;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.twinkle.common.model.Post;
import com.twinkle.common.model._MappingKit;
import com.twinkle.entity.BmobPost;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public enum CommUtils {

	INSTANCE;

	private HashMap<String, String> options;

	private AipOcr orcClient = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

	public static final String UUID = "020a2d70-cbbe-43fd-be3d-db7040d4a4e2";

	private ThreadPoolExecutor initExeCutorPool = new ThreadPoolExecutor(2, 5, 5, TimeUnit.SECONDS,
			new ArrayBlockingQueue<>(20));

	private HashMap<String, String> validatorHashMapParam() {

		if (options == null) {
			options = new HashMap<>();
		}

		if (options.size() == 0) {
			options.put("language_type", "ENG");
		}

		return options;

	}

	public Boolean regex(String text, String regex) {
		return Pattern.compile(regex).matcher(text).find();
	}

	public String mathChinese(String text) {
		return Pattern.compile("[^\u4E00-\u9FA5]").matcher(text).replaceAll(""); // [\u4E00-\u9FA5]是unicode2的中文区间
	}

	public String isServicePath() {
		return regex(System.getProperty(Constant.JavaProperty), Constant.JavaPath) ? Constant.demoPath
				: Constant.tomcatPath;
	}

	public String ocrPath() {
		return regex(System.getProperty(Constant.JavaProperty), Constant.JavaPath) ? Constant.orcPath
				: Constant.orcTomcatPath;
	}

	public String jwglUrl(String flag, String xh, String xm) {

		if (!"".equals(xm)) {
			try {
				xm = URLEncoder.encode(xh, "gb2312");
			} catch (Exception e) {
				return null;
			}
		}

		switch (flag) {
		case "Host":
			return " jwgl.cdnu.edu.cn";
		case "Index":
			return "http://jwgl.cdnu.edu.cn/xs_main.aspx?xh=" + xh;
		case "Info":
			return "http://jwgl.cdnu.edu.cn/xsgrxx.aspx?xh=" + xh + "&xm=" + xm + "&gnmkdm=N121501";
		case "TimeTable":
			return "http://jwgl.cdnu.edu.cn/xskbcx.aspx?xh=" + xh + "&xm=" + xm + "&gnmkdm=N121603";
		case "Score":
			return "http://jwgl.cdnu.edu.cn/xscj_gc.aspx?xh=" + xh + "&xm=" + xm + "&gnmkdm=N121605";
		default:
			return "";
		}
	}

	public String eolUrl(String subject) {
		return "http://eol.cdnu.edu.cn/eol" + subject + "&_=" + System.currentTimeMillis();
	}

	public void startRecordPlugin() {

		DruidPlugin druidPlugin = new DruidPlugin(
				"jdbc:mysql://127.0.0.1:3306/twinkle?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull", "root",
				"123");
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);

		_MappingKit.mapping(arp);
		druidPlugin.start();
		arp.start();

	}

	public void addToExeCutorPool(Runnable r) {

		initExeCutorPool.execute(r);
	}

	public String getRemoteAddr(Controller c) {

		if (c == null)
			return null;

		String ip = c.getRequest().getHeader("x-forwarded-for");

		if (validatorIp(ip)) {
			return ip;
		}
		ip = c.getRequest().getHeader("Proxy-Client-IP");
		if (validatorIp(ip)) {
			return ip;
		}
		ip = c.getRequest().getHeader("WL-Proxy-Client-IP");
		if (validatorIp(ip)) {
			return ip;
		}
		ip = c.getRequest().getHeader("HTTP_CLIENT_IP");
		if (validatorIp(ip)) {
			return ip;
		}
		ip = c.getRequest().getHeader("X-Real-IP");
		if (validatorIp(ip)) {
			return ip;
		}
		ip = c.getRequest().getRemoteAddr();
		if (validatorIp(ip)) {
			if (ip.equals(Constant.LOCALIP)) {
				return "127.0.0.1";
			}
			return ip;
		}

		return "";

	}

	private boolean validatorIp(String ip) {

		return ip != null && ip.length() > 0 && !ip.equals("unknown");

	}

	public String getOrcText(byte[] bytes) {

		return orcClient.basicGeneral(bytes, validatorHashMapParam()).toString();
	}

	public byte[] imgConvert(InputStream in) throws IOException {

		if (in == null)
			return null;

		BufferedImage bImage = ImageIO.read(in);

		BufferedImage newBImage = new BufferedImage(bImage.getWidth(), bImage.getHeight(), BufferedImage.TYPE_INT_RGB);

		newBImage.createGraphics().drawImage(bImage, 0, 0, Color.WHITE, null);

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		ImageIO.write(newBImage, "jpg", os);

		// ImageIO.write(newBImage, "jpg", new File("E:\\TWINKLE\\Desktop\\1.jpg"));

		return os.toByteArray();

	}

	public BmobPost generateBmobPost(com.twinkle.common.model.Post post) {

		if (post == null)
			return null;

		BmobPost bmobPost = new BmobPost();

		if (post.getAuthor() != null) {
			bmobPost.setAuthor(bmobPost.new User("_User", post.getAuthor()));
		}

		if (post.getTitle() != null) {
			bmobPost.setTitle(post.getTitle());
		}

		if (post.getContent() != null) {
			bmobPost.setContent(post.getContent());
		}

		if (post.getPic() != null) {

			String pic = post.getPic();

			if (StrKit.notBlank(pic)) {

				bmobPost.setPic(bmobPost.new Array(Arrays.asList(pic.replace("[", "").replace("]", "").split(","))));
			}
		}

		if (post.getTopic() != null) {

			bmobPost.setTopic(
					bmobPost.new Array(Arrays.asList(post.getTopic().replace("[", "").replace("]", "").split(","))));

		}

		if (post.getHot() != null) {
			bmobPost.setHot(post.getHot());
		}

		if (post.getPlace() != null) {
			bmobPost.setPlace(post.getPlace());
		}

		if (post.getUrl() != null) {
			bmobPost.setUrl(post.getUrl());
		}

		if (post.getTypes() != null) {
			bmobPost.setTypes(Integer.parseInt(post.getTypes()));
		}

		if (post.getNewsdate() != null) {
			bmobPost.setNewsdate(post.getNewsdate());
		}

		return bmobPost;

	}

	private OkHttpClient ok;

	private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public void postPostToBmobDatabase(Post post) {

		if (post == null)
			return;

		BmobPost bmobPost = CommUtils.INSTANCE.generateBmobPost(post);

		// System.out.println(com.alibaba.fastjson.JSON.toJSONString(bmobPost));

		ok = new OkHttpClient();

		RequestBody body = RequestBody.create(JSON, com.alibaba.fastjson.JSON.toJSONString(bmobPost));

		Request insert = new Request.Builder().url(Constant.BmobAddPostUrl)
				.addHeader("Content-Type", "application/json")
				.addHeader("X-Bmob-Application-Id", Constant.X_Bmob_Application_Id)
				.addHeader("X-Bmob-REST-API-Key", Constant.X_Bmob_REST_API_Key).post(body).build();

		try {
			ok.newCall(insert).execute();
			// System.out.println(r.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}