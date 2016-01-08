package com.example.mime;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.drawable.ScaleDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

public class GifView extends View {

	public static final int IMAGE_TYPE_UNKNOWN = 0;
	public static final int IMAGE_TYPE_STATIC = 1;
	public static final int IMAGE_TYPE_DYNAMIC = 2;

	public static final int DECODE_STATUS_UNDECODE = 0;
	public static final int DECODE_STATUS_DECODING = 1;
	public static final int DECODE_STATUS_DECODED = 2;

	private GifDecoder decoder;
	private Bitmap bitmap;

	public int imageType = IMAGE_TYPE_UNKNOWN;
	public int decodeStatus = DECODE_STATUS_UNDECODE;

	private int width;
	private int height;

	private long time;
	private int index;

	private int resId;
	private String filePath;

	private boolean playFlag = false;

	public GifView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	/**
	 * Constructor
	 */
	public GifView(Context context) {
		super(context);
	}

	private InputStream getInputStream() {
		if (filePath != null)
			try {
				return new FileInputStream(filePath);
			} catch (FileNotFoundException e) {
			}
		if (resId > 0)
			return getContext().getResources().openRawResource(resId);
		return null;
	}

	/**
	 * set gif file path
	 * 
	 * @param filePath
	 */
	public void setGif(String filePath) {
		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		setGif(filePath, bitmap);
	}

	/**
	 * set gif file path and cache image
	 * 
	 * @param filePath
	 * @param cacheImage
	 */
	public void setGif(String filePath, Bitmap cacheImage) {
		this.resId = 0;
		this.filePath = filePath;
		imageType = IMAGE_TYPE_UNKNOWN;
		decodeStatus = DECODE_STATUS_UNDECODE;
		playFlag = false;
		bitmap = cacheImage;

		width = bitmap.getWidth();
		height = bitmap.getHeight();

		setLayoutParams(new LayoutParams(width, height));

	}

	/**
	 * set gif resource id
	 * 
	 * @param resId
	 */
	public void setGif(int resId) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
	}

	/**
	 * set gif resource id and cache image
	 * 
	 * @param resId
	 * @param cacheImage
	 */
	public void setGif(int resId, Bitmap cacheImage) {
		this.filePath = null;
		this.resId = resId;
		imageType = IMAGE_TYPE_UNKNOWN;
		decodeStatus = DECODE_STATUS_UNDECODE;
		playFlag = false;
		bitmap = cacheImage;
		 width = bitmap.getWidth();
//		width = findViewById(R.id.editpic_preview).getWidth();
		 height = bitmap.getHeight();
//		height = findViewById(R.id.editpic_preview).getHeight();
		setLayoutParams(new LayoutParams(width, height));

	}

	private void decode() {
		release();
		index = 0;

		decodeStatus = DECODE_STATUS_DECODING;

		decoder = new GifDecoder();
		new Thread() {
			@Override
			public void run() {
				decoder.read(getInputStream());
				if (decoder.width == 0 || decoder.height == 0) {
					imageType = IMAGE_TYPE_STATIC;
				} else {
					imageType = IMAGE_TYPE_DYNAMIC;
				}
				postInvalidate();
				time = System.currentTimeMillis();
				decodeStatus = DECODE_STATUS_DECODED;
			}
		}.start();
	}

	public void release() {
		decoder = null;
	}

	/*
	 * �Ѿ���� �Ķ���ʹ� �θ��κ��� ������ ġ�������� �ǹ��Ѵ�. ���� �Ķ���Ϳ��� bit �����ڸ� ����ؼ� ���� ũ�⸦ ����
	 * ����ִ�. ���� MeasureSpec.getMode(spec) ���·� ������ ������ ���� 3������ �ִ�.
	 * MeasureSpec.AT_MOST : wrap_content (�� ������ ũ�⿡ ���� ũ�Ⱑ �޶���)
	 * MeasureSpec.EXACTLY : fill_parent, match_parent (�ܺο��� �̹� ũ�Ⱑ �����Ǿ���)
	 * MeasureSpec.UNSPECIFIED : MODE �� ���õ��� ���� ũ�Ⱑ �Ѿ�ö� (��κ� �� ���� ����)
	 * 
	 * fill_parent, match_parent �� ����ϸ� ���ܿ��� �̹� ũ�Ⱑ ���Ǿ� EXACTLY �� �Ѿ�´�. �̷��� ũ���
	 * MeasureSpec.getSize(spec) ���� �� �� �ִ�.
	 * 
	 * �� �޼ҵ忡���� setMeasuredDimension(measuredWidth,measuredHeight) �� ȣ���� �־�� �ϴµ�
	 * super.onMeasure() ������ �⺻���� �̸� �⺻���� ����ϴ� �Լ��� �����ϰ� �ִ�.
	 * 
	 * ���� xml ���� ũ�⸦ wrap_content �� �����ߴٸ� �� �Լ����� ũ�⸦ ����ؼ� ������ ����Ѵ�. �׷��� ������ ������
	 * fill_parent �� ������ �ȴ�.
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// height ��¥ ũ�� ���ϱ�
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = 0;
		switch (heightMode) {
		case MeasureSpec.UNSPECIFIED:// mode �� ���õ��� ���� ũ�Ⱑ �Ѿ�ö�
			heightSize = heightMeasureSpec;
			break;
		case MeasureSpec.AT_MOST:// wrap_content (�� ������ ũ�⿡ ���� ũ�Ⱑ �޶���)
			heightSize = 20;
			break;
		case MeasureSpec.EXACTLY:// fill_parent, match_parent (�ܺο��� �̹� ũ�Ⱑ
			// �����Ǿ���)
			heightSize = MeasureSpec.getSize(heightMeasureSpec);
			break;
		}
		heightSize = MeasureSpec.getSize(heightMeasureSpec);
		// width ��¥ ũ�� ���ϱ�
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = 0;
		switch (widthMode) {
		case MeasureSpec.UNSPECIFIED:// mode �� ���õ��� ���� ũ�Ⱑ �Ѿ�ö�
			widthSize = widthMeasureSpec;
			break;
		case MeasureSpec.AT_MOST:// wrap_content (�� ������ ũ�⿡ ���� ũ�Ⱑ �޶���)
			widthSize = 100;
			break;
		case MeasureSpec.EXACTLY:// fill_parent, match_parent (�ܺο��� �̹� ũ�Ⱑ
			// �����Ǿ���)
			widthSize = MeasureSpec.getSize(widthMeasureSpec);
			break;
		}

		// Log.w(Constants.TAG, "onMeasure(" + widthMeasureSpec + "," +
		// heightMeasureSpec + ")");
		width = widthSize;
		height = heightSize;

		setMeasuredDimension(widthSize, heightSize);
		// setLayoutParams(new LayoutParams(widthSize, heightSize));
	}

	/*
	 * onMeasure() �޼ҵ忡�� ������ width �� height �� ������ ���ø����̼� ��ü ȭ�鿡�� ���� �䰡 �׷�����
	 * bound �� �����ش�. �� �޼ҵ忡���� �Ϲ������� �� �信 ���� children ���� ��ġ��Ű�� ũ�⸦ �����ϴ� �۾��� �Ѵ�.
	 * ���������� �Ѿ���� �Ķ���Ͱ� ���ø����̼� ��ü�� �������� ��ġ�� �����ش�.
	 * 
	 * super �޼ҵ忡���� �ƹ��͵� �����ʱ⶧���� ���� �ʴ´�.
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// Log.w(Constants.TAG, "onLayout(" + changed + "," + left + "," + top +
		// "," + right + "," + bottom + ")");
	}

	/*
	 * �� ���� ũ�Ⱑ ����Ǿ����� ȣ��ȴ�.
	 * 
	 * super �޼ҵ忡���� �ƹ��͵� �����ʱ⶧���� ���� �ʴ´�.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// Log.w(Constants.TAG, "onSizeChanged(" + w + "," + h + "," + oldw +
		// "," + oldh + ")");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		final Paint p = new Paint();

		Bitmap rebitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), p);
		//
		if (decodeStatus == DECODE_STATUS_UNDECODE) {

			canvas.drawBitmap(bitmap, 0, 0, null);
			if (playFlag) {
				decode();
				invalidate();
			}
		} else if (decodeStatus == DECODE_STATUS_DECODING) {
			canvas.drawBitmap(bitmap, 0, 0, null);
			invalidate();
		} else if (decodeStatus == DECODE_STATUS_DECODED) {
			if (imageType == IMAGE_TYPE_STATIC) {
				canvas.drawBitmap(bitmap, 0, 0, null);
			} else if (imageType == IMAGE_TYPE_DYNAMIC) {
				if (playFlag) {
					long now = System.currentTimeMillis();

					if (time + decoder.getDelay(index) < now) {
						time += decoder.getDelay(index);
						incrementFrameIndex();
					}

					Bitmap bitmap = decoder.getFrame(index);

					if (bitmap != null) {
						canvas.drawBitmap(bitmap, 0, 0, null);
					}
					invalidate();
				} else {
					Bitmap bitmap = decoder.getFrame(index);
					canvas.drawBitmap(bitmap, 0, 0, null);
				}
			} else {
				canvas.drawBitmap(bitmap, 0, 0, null);
			}
		}
	}

	private void incrementFrameIndex() {
		index++;

		if (index >= decoder.getFrameCount()) {
			index = 0;
		}

	}

	private void decrementFrameIndex() {
		index--;
		if (index < 0) {
			index = decoder.getFrameCount() - 1;
		}
	}

	public void play() {
		time = System.currentTimeMillis();
		playFlag = true;
		invalidate();
	}

	public void pause() {
		playFlag = false;
		invalidate();
	}

	public void stop() {
		playFlag = false;
		index = 0;
		invalidate();
	}

	public void nextFrame() {
		if (decodeStatus == DECODE_STATUS_DECODED && index < decoder.getFrameCount()) {
			incrementFrameIndex();
			invalidate();
		}
	}

	public void prevFrame() {
		if (decodeStatus == DECODE_STATUS_DECODED) {
			decrementFrameIndex();
			invalidate();
		}
	}
}