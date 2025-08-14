import type { AuthorType } from "./authorType";
import type { GenreType } from "./genreType";

export interface BookType {
  id: string | null;
  title: string | null;
  author: AuthorType | null;
  genre: GenreType | null;
  createdAt: Date | null;
}
