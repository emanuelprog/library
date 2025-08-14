import { api } from 'boot/axios';
import type { GenreType } from 'src/types/genreType';

export async function listAllGenre() {
  const response = await api.get('/genres');

  return response.data.obj;
}

export async function createGenre(genre: GenreType) {
  const response = await api.post('/genres', genre);

  return response.data.obj;
}

export async function updateGenre(id: string, genre: GenreType) {
  const response = await api.put(`/genres/${id}`, genre);

  return response.data.obj;
}

export async function removeGenre(id: string) {
  await api.delete(`/genres/${id}`);
}
