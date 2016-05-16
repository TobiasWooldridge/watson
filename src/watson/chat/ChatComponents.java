package watson.chat;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.ITextComponent;
import watson.PrivateFieldsWatson;

// ----------------------------------------------------------------------------
/**
 * utility methods for dealing with ITextComponent.
 */
public class ChatComponents
{
  // --------------------------------------------------------------------------
  /**
   * Return true of the chat has associated events (click, hover).
   * 
   * The current Watson highlighting implementation only supports old style
   * formatting codes and so can only highlight ITextComponents that don't have
   * associated events.
   * 
   * @return true of the chat has associated events (click, hover).
   */
  public static boolean hasEvents(ITextComponent chat)
  {
    return chat.getStyle().getClickEvent() != null ||
           chat.getStyle().getHoverEvent() != null;
  }

  // --------------------------------------------------------------------------
  /**
   * Return an array containing the specified ITextComponent and all its
   * siblings.
   * 
   * @return an array containing the specified ITextComponent and all its
   *         siblings.
   */
  @SuppressWarnings("unchecked")
  public static ArrayList<ITextComponent> getComponents(ITextComponent chat)
  {
    ArrayList<ITextComponent> components = new ArrayList<ITextComponent>();
    for (Object o : chat)
    {
      ITextComponent component = (ITextComponent) o;
      ITextComponent copy = new TextComponentString(component.getUnformattedText());
      copy.setStyle(component.getStyle().createDeepCopy());
      components.add(copy);
    }
    return components;
  }

  // --------------------------------------------------------------------------
  /**
   * Return an array containing all of the components in the input array and all
   * of their siblings, in their natural order.
   * 
   * @param components an array of ITextComponents.
   * @return an array containing all of the components in the input array and
   *         all of their siblings, in their natural order.
   */
  public static ArrayList<ITextComponent> flatten(ArrayList<ITextComponent> components)
  {
    ArrayList<ITextComponent> result = new ArrayList<ITextComponent>();
    for (ITextComponent component : components)
    {
      result.addAll(getComponents(component));
    }
    return result;
  }

  // --------------------------------------------------------------------------
  /**
   * Convert an array of ITextComponents into a single ITextComponent with all
   * the individual components as siblings.
   * 
   * @param components the array of components to be added.
   * @return an ITextComponent containing copies of all of the components in the
   *         array.
   */
  public static ITextComponent toChatComponent(ArrayList<ITextComponent> components)
  {
    ArrayList<ITextComponent> all = flatten(components);
    if (components.size() == 0)
    {
      return new TextComponentString("");
    }
    else
    {
      ITextComponent result = all.get(0);
      for (int i = 1; i < all.size(); ++i)
      {
        ITextComponent component = all.get(i);

        if (component.getUnformattedText().length() != 0 || !component.getStyle().isEmpty())
        {
          result.appendSibling(component);
        }
      }
      return result;
    }
  } // toChatComponent

  // --------------------------------------------------------------------------
  /**
   * Map a formatting character to the corresponding Minecraft
   * TextFormatting.
   * 
   * @param code the formatting code.
   * @return  the corresponding enum value.
   */
  public static TextFormatting getTextFormatting(char code)
  {
    return _formatCharToEnum.get(code);
  }

  // --------------------------------------------------------------------------
  /**
   * Dump information about the ITextComponent to standard output.
   * 
   * @patam component the component.
   */
  public static void dump(ITextComponent component)
  {
    System.out.println("Formatted: " + component.getFormattedText());
    dump(flatten(getComponents(component)));
  }

  // --------------------------------------------------------------------------
  /**
   * Dump information about the ITextComponent to standard output.
   * 
   * @patam component the component.
   */
  public static void dump(ArrayList<ITextComponent> components)
  {
    System.out.println("Dump: " + toChatComponent(components).getFormattedText());
    for (int i = 0; i < components.size(); ++i)
    {
      ITextComponent c = components.get(i);
      System.out.println(i + ": " + hasEvents(c) + ": \"" + c.getFormattedText() + "\" "
              + c.getUnformattedComponentText().length() + " "
              + c.getStyle().isEmpty() + " " + c.getStyle().toString());
    }
  }

  // --------------------------------------------------------------------------
  /**
   * Map formatting character to the corresponding Minecraft TextFormatting.
   */
  private static HashMap<Character, TextFormatting> _formatCharToEnum = new HashMap<Character, TextFormatting>();

  static
  {
    for (TextFormatting format : TextFormatting.values())
    {
      _formatCharToEnum.put(PrivateFieldsWatson.formattingCode.get(format), format);
    }
  }
} // class ChatComponents