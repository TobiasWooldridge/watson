package watson.chat;

import java.util.regex.Matcher;

import net.minecraft.util.text.ITextComponent;

// ----------------------------------------------------------------------------
/**
 * Interface to methods called when a regular expression Matcher matches()
 * unformatted chat.
 */
public interface IMatchedChatHandler
{
  /**
   * Callback method when a chat (unformatted), is matched.
   * 
   * @param chat the chat.
   * @param m the Matcher that matches.
   * @return true if the chat should be echoed in the client GUI; false if it
   *         should be filtered out.
   */
  public boolean onMatchedChat(ITextComponent chat, Matcher m);
}
