package it.unibo.oop.lab.lambda.ex02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 */
public final class MusicGroupImpl implements MusicGroup {

    private final Map<String, Integer> albums = new HashMap<>();
    private final Set<Song> songs = new HashSet<>();

    @Override
    public void addAlbum(final String albumName, final int year) {
        this.albums.put(albumName, year);
    }

    @Override
    public void addSong(final String songName, final Optional<String> albumName, final double duration) {
        if (albumName.isPresent() && !this.albums.containsKey(albumName.get())) {
            throw new IllegalArgumentException("invalid album name");
        }
        this.songs.add(new MusicGroupImpl.Song(songName, albumName, duration));
    }

    @Override
    public Stream<String> orderedSongNames() {
        final List<String> songNameList = new ArrayList<>();
        this.songs.forEach(s -> songNameList.add(s.getSongName()));
        return songNameList.stream().sorted();
    }

    @Override
    public Stream<String> albumNames() {
        final List<String> albumListname = new ArrayList<>();
        this.albums.forEach((name, year) -> albumListname.add(name));
        return albumListname.stream();
    }

    @Override
    public Stream<String> albumInYear(final int year) {
        return this.albums.keySet().stream()
                                   .filter(nameAlbum -> this.albums.get(nameAlbum) == year);
    }

    @Override
    public int countSongs(final String albumName) {
        return (int) this.songs.stream()
                               .filter(song -> song.albumName.equals(Optional.of(albumName)))
                               .count();
    }

    @Override
    public int countSongsInNoAlbum() {
        return (int) this.songs.stream()
                               .filter(song -> song.albumName.isEmpty())
                               .count();
    }

    @Override
    public OptionalDouble averageDurationOfSongs(final String albumName) {
        return this.songs.stream()
                         .filter(song -> song.albumName.equals(Optional.ofNullable(albumName)))
                         .mapToDouble(song -> song.duration)
                         .average();
    }

    @Override
    public Optional<String> longestSong() {
        return null;
    }

    @Override
    public Optional<String> longestAlbum() {
        return null;
    }

    private static final class Song {

        private final String songName;
        private final Optional<String> albumName;
        private final double duration;
        private int hash;

        Song(final String name, final Optional<String> album, final double len) {
            super();
            this.songName = name;
            this.albumName = album;
            this.duration = len;
        }

        public String getSongName() {
            return songName;
        }

        public Optional<String> getAlbumName() {
            return albumName;
        }

        public double getDuration() {
            return duration;
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = songName.hashCode() ^ albumName.hashCode() ^ Double.hashCode(duration);
            }
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Song) {
                final Song other = (Song) obj;
                return albumName.equals(other.albumName) && songName.equals(other.songName)
                        && duration == other.duration;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Song [songName=" + songName + ", albumName=" + albumName + ", duration=" + duration + "]";
        }

    }

}
