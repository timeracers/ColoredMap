package coloredmap;

import basemod.IUIElement;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.function.Consumer;

class ImageButton implements IUIElement
{
    private Texture image;
    private int x;
    private int y;
    private int w;
    private int h;
    private Hitbox hitbox;
    public Consumer<ImageButton> click;

    public ImageButton (String url, int x, int y, int width, int height, Consumer<ImageButton> click) {
        this.image = new Texture(url);
        this.x = (int)(Settings.scale * x);
        this.y = (int)(Settings.scale * y);
        this.w = (int)(Settings.scale * width);
        this.h = (int)(Settings.scale * height);
        this.hitbox = new Hitbox(this.x, this.y, this.w, this.h);
        this.click = click;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(image, x, y, w, h);
        hitbox.render(sb);
    }

    @Override
    public void update() {
        hitbox.update();
        if(this.hitbox.hovered && InputHelper.justClickedLeft) {
            CardCrawlGame.sound.play("UI_CLICK_1");
            click.accept(this);
        }
    }

    @Override
    public int renderLayer() {
        return 1;
    }

    @Override
    public int updateOrder() {
        return 1;
    }
}
