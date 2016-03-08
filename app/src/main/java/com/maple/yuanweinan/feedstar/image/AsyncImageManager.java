package com.maple.yuanweinan.feedstar.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

/**
 * 图片管理器, 
 * @author matt
 * @备注 业务内容可在此处添加, 勿修改ImageLoader 
 * @备注 简化了图片加载接口, 所有loadXXX方法都调用这里的, 不要调用父类
 */
public class AsyncImageManager extends AsyncListImageLoader {
	//是否自动更新分组标签
	private final static boolean IS_AUTO_UPDATE_GROUPLABEL = true;
	//设置imageview时用到的key
	public final static int IMAGEVIEW_TAG_KEY = -123456;

	public final static String SDCARD = Environment.getExternalStorageDirectory().getPath();
	/**
	 * 需要保存到SD卡的图片, 统一保存在此目录, 路径尾部须带路径分隔符“/”
	 */
	public static final String IMAGE_DIR = SDCARD + "/feedstar/images/";
	public static String sImageDir = null;
	private static AsyncImageManager sInstance = null;

	private AsyncImageManager(Context context, IImageCache imageCache) {
		super(imageCache);
		sImageDir = IMAGE_DIR;
	
	}
	
	public static AsyncImageManager getInstance(Context context) {
		if (null == sInstance) {
			int cacheSize = LruImageCache.getImagesMaxMemorySizeSuggested(context);
			IImageCache secondaryCache = new ImageWeakCache();
			sInstance = new AsyncImageManager(context, new LruImageCache(cacheSize, secondaryCache));
		}
		return sInstance;
	}
	
	/**
	 * 设置当前分组标签
	 * @param groupLabel
	 */
	private void setCurrentGroupLabel(String groupLabel) {
		//只允许有一个当前标签，简化逻辑
		mLabelManager.clearLabel();
		mLabelManager.addLabel(groupLabel);
	}
	
	/**
	 * 设置ImageView显示图片
	 * @param imageView 外部务必不要设置此ImageView的key为{@link #IMAGEVIEW_TAG_KEY}的Tag，否则不能成功显示图片
	 * @param groupLabel 图片分组标签，用于图片加载优先级
	 * @param imgUrl 图片URL
	 */
	@SuppressLint("NewApi")
	public void setImageView(final ImageView imageView, String groupLabel, String imgUrl, ImageScaleConfig scaleCfg) {
		imageView.setTag(IMAGEVIEW_TAG_KEY, imgUrl);
		loadImage(groupLabel, imgUrl, scaleCfg, new SimpleImageLoadResultCallBack() {
			
			@Override
			public void imageLoadSuccess(String imgUrl, Bitmap bmp, String imgSavePath) {
				Object tag = imageView.getTag(IMAGEVIEW_TAG_KEY);
				if (tag instanceof String && tag.equals(imgUrl)) {
					imageView.setImageBitmap(bmp);
				}
			}
		});
	}
	
	/**
	 * 添加遮罩转换图片成圆形
	 * @param imageView 外部务必不要设置此ImageView的key为{@link #IMAGEVIEW_TAG_KEY}的Tag，否则不能成功显示图片
	 * @param groupLabel 图片分组标签，用于图片加载优先级
	 * @param imgUrl 图片URL
	 */
	@SuppressLint("NewApi")
	public void setImageViewRound(final ImageView imageView, String groupLabel, String imgUrl, ImageScaleConfig scaleCfg, int width, int height) {
		imageView.setTag(IMAGEVIEW_TAG_KEY, imgUrl);
		loadImageRound(groupLabel, imgUrl, scaleCfg, width, height, new SimpleImageLoadResultCallBack() {
			
			@Override
			public void imageLoadSuccess(String imgUrl, Bitmap bmp, String imgSavePath) {
				Object tag = imageView.getTag(IMAGEVIEW_TAG_KEY);
				if (tag instanceof String && tag.equals(imgUrl)) {
					imageView.setImageBitmap(bmp);
				}
			}
		});
	}
	
	/**
	 * 专门针对列表项里ImageView加载图片的方法
	 * @param imageView 外部务必不要设置此ImageView的key为{@link #IMAGEVIEW_TAG_KEY}的Tag，否则不能成功显示图片
	 * @param position 列表项位置
	 * @param groupLabel 图片分组标签，用于图片加载优先级
	 * @param imgUrl 图片URL
	 * @param scaleCfg 图片压缩配置
	 */
	@SuppressLint("NewApi")
	public void setImageViewForList(final ImageView imageView, int position, String groupLabel, String imgUrl, ImageScaleConfig scaleCfg) {
		imageView.setTag(IMAGEVIEW_TAG_KEY, imgUrl);
		loadImageForList(position, groupLabel, imgUrl, scaleCfg, new SimpleImageLoadResultCallBack() {
			
			@Override
			public void imageLoadSuccess(String imgUrl, Bitmap bmp, String imgSavePath) {
				Object tag = imageView.getTag(IMAGEVIEW_TAG_KEY);
				if (tag instanceof String && tag.equals(imgUrl)) {
					imageView.setImageBitmap(bmp);
				}
			}
		});
	}
	
	/**
	 * 加载图片，图片通过回调返回
	 * @param groupLabel 图片分组标签，用于图片加载优先级
	 * @param imgUrl 图片URL
	 * @param scaleCfg 图片压缩配置
	 * @param callBack 回调到UI线程
	 * @return
	 */
	public void loadImage(String groupLabel, String imgUrl, ImageScaleConfig scaleCfg, AsyncImageLoadResultCallBack callBack) {
		if (null == imgUrl) {
			return ;
		}
		if (IS_AUTO_UPDATE_GROUPLABEL) {
			setCurrentGroupLabel(groupLabel);
		}
		ImageLoadRequest request = new ImageLoadRequest(imgUrl, sImageDir, "" + imgUrl.hashCode());
		request.mGroupLabel = groupLabel;
		request.mCallBack = callBack;
		
		loadImage(request);
	}
	
	/**
	 * 加载图片，图片通过回调返回
	 * @param groupLabel 图片分组标签，用于图片加载优先级
	 * @param imgUrl 图片URL
	 * @param scaleCfg 图片压缩配置
	 * @param callBack 回调到UI线程
	 * @return
	 */
	public void loadImageRound(String groupLabel, String imgUrl, ImageScaleConfig scaleCfg, final int width, final int height, AsyncImageLoadResultCallBack callBack) {
		if (null == imgUrl) {
			return ;
		}
		if (IS_AUTO_UPDATE_GROUPLABEL) {
			setCurrentGroupLabel(groupLabel);
		}
		ImageLoadRequest request = new ImageLoadRequest(imgUrl, sImageDir, "" + imgUrl.hashCode());
		request.mGroupLabel = groupLabel;
		request.mCallBack = callBack;
		request.mNetOperator = new AsyncNetBitmapOperator() {

			@Override
			public Bitmap operateBitmap(Bitmap bmp) {
				
				return toRoundBitmap(bmp, width, height);
			}
		};
		
		loadImage(request);
	}
	
	/**
	 * 专门针对列表项加载图片的方法
	 * @param position 列表项位置
	 * @param groupLabel 图片分组标签，用于图片加载优先级
	 * @param imgUrl 图片URL
	 * @param scaleCfg 图片压缩配置
	 * @param callBack 回调到UI线程
	 * @return
	 * @备注: 在列表停止滚动时才去加载图片
	 */
	public void loadImageForList(int position, String groupLabel, String imgUrl, ImageScaleConfig scaleCfg, AsyncImageLoadResultCallBack callBack) {
		if (null == imgUrl) {
			return;
		}
		if (IS_AUTO_UPDATE_GROUPLABEL) {
			setCurrentGroupLabel(groupLabel);
		}
		ImageLoadRequest request = new ImageLoadRequest(imgUrl, sImageDir, "" + imgUrl.hashCode());
		request.mGroupLabel = groupLabel;
		request.mCallBack = callBack;
		loadImageForList(position, request);
	}
	
	/**
	 * <br> 功能详述：添加遮罩转换图片成圆形 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap, int scaleX, int scaleY) {
		return null;
		/*
		
		Bitmap output = Bitmap.createBitmap(scaleX, scaleY, Config.ARGB_8888);
		Bitmap maskBitmap = BitmapFactory.decodeResource(GameHallFacade.getApplicationContext().getResources(), R.drawable.appcenter_round_mask);
		bitmap = createScaledBitmap(bitmap, scaleX, scaleY);
		Bitmap maskBitmap2 = createScaledBitmap(maskBitmap, scaleX, scaleY);

		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();

		canvas.drawBitmap(maskBitmap2, 0, 0, paint);
		paint.setColor(Color.WHITE);
		paint.setFilterBitmap(false);
		paint.setAntiAlias(true);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, 0, 0, paint);
		paint.setXfermode(null);
		canvas.drawARGB(0, 0, 0, 0);
		output = createScaledBitmap(output, scaleX, scaleY);	
		return output;
	*/}	
	/**
	 * 缩放位图
	 * @param bmp
	 * @param scaleWidth
	 * @param scaleHeight
	 * @return
	 */
	public static final Bitmap createScaledBitmap(Bitmap bmp, int scaleWidth, int scaleHeight) {
		Bitmap pRet = null;
		if (null == bmp) {
			return pRet;
		}
		if (scaleWidth == bmp.getWidth() && scaleHeight == bmp.getHeight()) {
			return bmp;
		}
		try {
			pRet = Bitmap.createScaledBitmap(bmp, scaleWidth, scaleHeight, true);
		} catch (OutOfMemoryError e) {
			pRet = null;
		} catch (Exception e) {
			pRet = null;
		}

		return pRet;
	}

}
