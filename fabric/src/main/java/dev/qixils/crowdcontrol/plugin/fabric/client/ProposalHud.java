package dev.qixils.crowdcontrol.plugin.fabric.client;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.class_8471;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static net.minecraft.util.math.MathHelper.ceil;

public class ProposalHud extends DrawableHelper {
	public final ProposalHandler handler;
	private static final int MARGIN = 6;
	private static final int PADDING = 4;
	private static final int TEXT_SIZE = 10;
	private static final float HEADER_TEXT_SCALE = 1.25f;
	private static final float BODY_TEXT_SCALE = 1.0f;
	private static final int TEXT_MARGIN = 2;
	private static final int TEXT_COLOR = 0xFFFFFF;
	private static final int WINNING_TEXT_COLOR = NamedTextColor.GREEN.value();
	private static final int BACKGROUND_COLOR = 0x60000000;
	private static final int TIME_BAR_COLOR = NamedTextColor.GRAY.value() | 0xFF000000;
	private static final int COOLDOWN_BAR_COLOR = NamedTextColor.DARK_GRAY.value() | 0xFF000000;
	private static final int VOTE_BAR_COLOR = NamedTextColor.YELLOW.value() | 0xFF000000;
	private static final int BAR_HEIGHT = 2;
	private static final int BAR_MIN_WIDTH = 10;
	private static final int PERCENT_PADDING = 6;
	private static final int PERCENT_COLOR = NamedTextColor.GRAY.value();

	public ProposalHud(ProposalHandler handler) {
		this.handler = handler;
	}

	private static Text optionText(String voteCommand, OptionWrapper option) {
		return Text.translatable("options.generic_value", voteCommand, option.data().comp_1359());
	}

	private static Text optionVotesText(int votes, int totalVotes) {
		return Text.literal(ceil(votes / (float) totalVotes * 100f) + "% (" + votes + ")");
	}

	public void render(MatrixStack matrixStack, TextRenderer textRenderer) {
		ProposalVote vote = handler.getCurrentProposal();
		if (vote == null)
			return;
		class_8471.class_8474 proposal = vote.getProposal();
		if (proposal == null)
			return;
		int secondsLeft = Math.max(ceil(vote.getRemainingTicks() / 20f), 0);
		Map<String, Integer> votes = new HashMap<>(vote.voteCounts());
		int totalVotes = Math.max(1, votes.values().stream().mapToInt(Integer::intValue).sum());
		matrixStack.push();
		matrixStack.translate(MARGIN, MARGIN, 0);
		// render background ...
		MutableText proposalText = proposal.method_51082().comp_1357().comp_1361().copy();
		if (secondsLeft > 0)
			proposalText.append(" (" + secondsLeft + "s)");
		int maxOptionWidth = vote.getOptions().entrySet().stream()
				.mapToInt(entry -> textRenderer.getWidth(optionText(entry.getKey(), entry.getValue())))
				.max().orElse(0) + PERCENT_PADDING + textRenderer.getWidth(optionVotesText(totalVotes, totalVotes));
		int maxWidth = Math.max(ceil(textRenderer.getWidth(proposalText) * HEADER_TEXT_SCALE), ceil(maxOptionWidth * BODY_TEXT_SCALE));
		int height = (PADDING*2) + ceil((TEXT_SIZE + BAR_HEIGHT) * HEADER_TEXT_SCALE) + TEXT_MARGIN + ceil((TEXT_SIZE + BAR_HEIGHT + TEXT_MARGIN) * vote.getOptions().size() * BODY_TEXT_SCALE) - 2;
		fill(matrixStack, 0, 0, maxWidth + (PADDING*2), height, BACKGROUND_COLOR);
		// render text ...
		matrixStack.translate(PADDING, PADDING, 0);
		matrixStack.scale(HEADER_TEXT_SCALE, HEADER_TEXT_SCALE, 1);
		textRenderer.drawWithShadow(matrixStack, proposalText, 0, 0, TEXT_COLOR);
		matrixStack.translate(0, TEXT_SIZE, 0);
		matrixStack.scale(1 / HEADER_TEXT_SCALE, 1 / HEADER_TEXT_SCALE, 1);
		int cooldownBarWidth = vote.isClosed()
				? ceil(maxWidth * (handler.proposalCooldown / (float) ProposalVote.COOLDOWN))
				: maxWidth;
		int timeBarWidth = ceil(maxWidth * vote.getRemainingTimePercentage());
		fill(matrixStack, 0, 0, cooldownBarWidth, BAR_HEIGHT, COOLDOWN_BAR_COLOR);
		fill(matrixStack, 0, 0, timeBarWidth, BAR_HEIGHT, TIME_BAR_COLOR);
		matrixStack.pop();
		matrixStack.push();
		matrixStack.translate(MARGIN + PADDING, MARGIN + PADDING + ((TEXT_SIZE + BAR_HEIGHT) * HEADER_TEXT_SCALE) + TEXT_MARGIN, 0);
		matrixStack.scale(BODY_TEXT_SCALE, BODY_TEXT_SCALE, 1);
		String winnerKey = vote.isClosed() ? vote.getWinnerKey() : null;
		for (Map.Entry<String, OptionWrapper> entry : vote.getOptions().entrySet()) {
			String voteCommand = entry.getKey();
			OptionWrapper option = entry.getValue();
			int votesForOption = votes.getOrDefault(voteCommand, 0);
			// draw text
			textRenderer.drawWithShadow(matrixStack, optionText(voteCommand, option), 0, 0, Objects.equals(winnerKey, voteCommand) ? WINNING_TEXT_COLOR : TEXT_COLOR);
			Text optionVotesText = optionVotesText(votesForOption, totalVotes);
			textRenderer.drawWithShadow(matrixStack, optionVotesText, ceil((maxWidth - textRenderer.getWidth(optionVotesText)) / BODY_TEXT_SCALE), 0, PERCENT_COLOR);
			matrixStack.translate(0, TEXT_SIZE, 0);
			// draw bar
			int barWidth = BAR_MIN_WIDTH + ceil(((maxWidth / BODY_TEXT_SCALE) - BAR_MIN_WIDTH) * (votesForOption / (float) totalVotes));
			fill(matrixStack, 0, 0, barWidth, BAR_HEIGHT, VOTE_BAR_COLOR);
			// translate
			matrixStack.translate(0, BAR_HEIGHT + TEXT_MARGIN, 0);
		}
		matrixStack.pop();
	}
}
