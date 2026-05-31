package com.example.wordmaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ConfettiView extends View {

    private final List<Confetto> confettiList = new ArrayList<>();
    private final Random random = new Random();
    private final int[] colors = {0xFFFFC107, 0xFFFF5722, 0xFFE91E63, 0xFF2196F3, 0xFF4CAF50, 0xFFFFD700};
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ConfettiView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private static class Confetto {
        float x, y, vX, vY, rotation, rotationSpeed, size, widthScale;
        int color;

        Confetto(float x, float y, float vX, float vY, int color, float rotation, float rotationSpeed, float size) {
            this.x = x;
            this.y = y;
            this.vX = vX;
            this.vY = vY;
            this.color = color;
            this.rotation = rotation;
            this.rotationSpeed = rotationSpeed;
            this.size = size;
        }
    }

    public void startConfetti(int count) {
        confettiList.clear();
        for (int i = 0; i < count; i++) {
            confettiList.add(new Confetto(
                random.nextFloat() * getWidth(),
                -random.nextFloat() * getHeight(),
                (random.nextFloat() - 0.5f) * 10f,
                random.nextFloat() * 15f + 10f,
                colors[random.nextInt(colors.length)],
                random.nextFloat() * 360f,
                (random.nextFloat() - 0.5f) * 10f,
                random.nextFloat() * 20f + 15f
            ));
        }
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Iterator<Confetto> iterator = confettiList.iterator();

        while (iterator.hasNext()) {
            Confetto c = iterator.next();

            // Update Physics
            c.y += c.vY;
            c.x += c.vX;
            c.vY += 0.2f; // Gravity
            c.rotation += c.rotationSpeed;
            c.widthScale = (float) Math.sin(c.rotation * 0.1);

            // Draw Confetto
            paint.setColor(c.color);
            canvas.save();
            canvas.translate(c.x, c.y);
            canvas.rotate(c.rotation);
            canvas.scale(c.widthScale, 1f);
            canvas.drawRect(-c.size / 2, -c.size / 2, c.size / 2, c.size / 2, paint);
            canvas.restore();

            // Remove if off screen
            if (c.y > getHeight()) {
                iterator.remove();
            }
        }

        if (!confettiList.isEmpty()) {
            postInvalidateOnAnimation();
        }
    }
}