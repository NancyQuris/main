package seedu.jxmusic.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.jxmusic.logic.parser.CliSyntax.PREFIX_PLAYLIST;
import static seedu.jxmusic.logic.parser.CliSyntax.PREFIX_TRACK;

import java.util.List;

import seedu.jxmusic.model.Model;
import seedu.jxmusic.model.Name;
import seedu.jxmusic.model.Playlist;
import seedu.jxmusic.model.Track;

/**
 * Add a track to target playlist.
 */
public class TrackAddCommand extends Command {
    public static final String COMMAND_PHRASE = "track add";
    public static final String MESSAGE_SUCCESS = "Track %1$s added to playlist %2$s";
    public static final String MESSAGE_USAGE = COMMAND_PHRASE + ": Adds a new track to the playlist identified "
        + "by the name of the track and playlist."
        + "Playlist will be modified with new track.\n"
        + "Parameters: [" + PREFIX_PLAYLIST + "PLAYLIST]"
        + "[" + PREFIX_TRACK + "TRACK]...\n"
        + "Example: " + COMMAND_PHRASE
        + PREFIX_PLAYLIST + "rockPlaylist"
        + PREFIX_TRACK + "rockNRoll";
    public static final String MESSAGE_DUPLICATE_TRACK = "This track already exists in the playlist";
    public static final String MESSAGE_PLAYLIST_DOES_NOT_EXIST = "This playlist does not exist";

    private Track trackToAdd;
    private Playlist targetPlaylist;

    public TrackAddCommand(Track trackToAdd, Playlist targetPlaylist) {
        requireNonNull(trackToAdd);
        requireNonNull(targetPlaylist);
        this.trackToAdd = trackToAdd;
        this.targetPlaylist = targetPlaylist;
    }

    /**
     * For immutability
     * @param targetPlaylist
     * Returns a copy of targetPlaylist
     */
    private Playlist copyPlaylist(Playlist targetPlaylist) {
        List<Track> tracks = targetPlaylist.getTracks();
        Name nameCopy = targetPlaylist.getName();
        Playlist playlistCopy = new Playlist(nameCopy);
        for (Track track : tracks) {
            playlistCopy.addTrack(track);
        }
        return playlistCopy;
    }

    /**
     * @param playlist an existing playlist
     * @param newTrack a new track to be added
     * Returns boolean depending if newTrack already exists in playlist
     */
    private boolean trackExists(Playlist playlist, Track newTrack) {
        for (Track track : playlist.getTracks()) {
            if (track.getFileName().equalsIgnoreCase(newTrack.getFileName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CommandResult execute(Model model) {
        Playlist updatedPlaylist;

        // check if playlist exists
        if (!model.hasPlaylist(targetPlaylist)) {
            return new CommandResult(MESSAGE_PLAYLIST_DOES_NOT_EXIST);
        }
        updatedPlaylist = copyPlaylist(targetPlaylist);
        // check if track exists in existing playlist
        if (trackExists(targetPlaylist, trackToAdd)) {
            return new CommandResult(MESSAGE_DUPLICATE_TRACK);
        }
        updatedPlaylist.addTrack(trackToAdd);
        model.updatePlaylist(targetPlaylist, updatedPlaylist);
        return new CommandResult(String.format(MESSAGE_SUCCESS, trackToAdd, targetPlaylist.getName()));
    }

}
