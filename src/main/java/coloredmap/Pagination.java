package coloredmap;

import basemod.IUIElement;
import basemod.ModColorDisplay;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

class Pagination implements IUIElement
{
    private ImageButton next;
    private ImageButton prior;
    private int page = 0;
    private int elementsPerPage;
    private List<ModColorDisplay> elements;

    Pagination(ImageButton next, ImageButton prior, int rows, int columns, int width, int height, List<ModColorDisplay> elements) {
        next.click = (b) -> page++;
        prior.click = (b) -> page--;
        this.next = next;
        this.prior = prior;
        this.elements = new ArrayList<>();
        elementsPerPage = rows * columns;
        for (int i = 0; i < elements.size(); i++) {
            ModColorDisplay element = elements.get(i);
            ModColorDisplay newElement = new ModColorDisplay(
                element.x + width * (i % columns),
                element.y - height * ((i % elementsPerPage - i % columns) / columns),
                element.texture,
                element.outline,
                element.click);
            newElement.r = element.r;
            newElement.g = element.g;
            newElement.b = element.b;
            newElement.a = element.a;
            newElement.aOutline = element.aOutline;
            newElement.rOutline = element.rOutline;
            newElement.gOutline = element.gOutline;
            newElement.bOutline = element.bOutline;
            this.elements.add(newElement);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if(page != 0)
            prior.render(spriteBatch);
        if((page + 1) * elementsPerPage < elements.size())
            next.render(spriteBatch);
        for (ModColorDisplay element : elements.subList(page * elementsPerPage, Math.min((page + 1) * elementsPerPage, elements.size())))
            element.render(spriteBatch);
    }

    @Override
    public void update() {
        if(page != 0)
            prior.update();
        if((page + 1) * elementsPerPage < elements.size())
            next.update();
        for (ModColorDisplay element : elements.subList(page * elementsPerPage, Math.min((page + 1) * elementsPerPage, elements.size())))
            element.update();
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
