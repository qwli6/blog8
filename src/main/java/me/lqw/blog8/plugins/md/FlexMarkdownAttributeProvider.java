package me.lqw.blog8.plugins.md;

import com.vladsch.flexmark.ast.AutoLink;
import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataKeyBase;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.html.MutableAttributes;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FlexMarkdownAttributeProvider {


    static class ImageExtension implements HtmlRenderer.HtmlRendererExtension {

        @Override
        public void rendererOptions(@NotNull MutableDataHolder mutableDataHolder) {

//            Map<? extends DataKeyBase<?>, Object> all = mutableDataHolder.getAll();



        }

        @Override
        public void extend(HtmlRenderer.@NotNull Builder builder, @NotNull String s) {
            builder.attributeProviderFactory(ImageAttributeProvider.factory());
        }

        static ImageExtension create(){
            return new ImageExtension();
        }
    }

    static class ImageAttributeProvider implements AttributeProvider {

        @Override
        public void setAttributes(@NotNull Node node, @NotNull AttributablePart attributablePart,
                                  @NotNull MutableAttributes mutableAttributes) {

            if(node instanceof Image && attributablePart == AttributablePart.NODE ){

            }

        }

        static AttributeProviderFactory factory(){

            return new IndependentAttributeProviderFactory() {
                @Override
                public @NotNull AttributeProvider apply(@NotNull LinkResolverContext linkResolverContext) {
                    return new ImageAttributeProvider();
                }
            };
        }
    }
}
