import { api } from 'boot/axios';
import type { AuthorType } from 'src/types/authorType';

export async function listAllAuthor() {
  const response = await api.get('/authors');

  return response.data.obj;
}

export async function createAuthor(author: AuthorType) {
  const response = await api.post('/authors', author);

  return response.data.obj;
}

export async function updateAuthor(id: string, author: AuthorType) {
  const response = await api.put(`/authors/${id}`, author);

  return response.data.obj;
}

export async function removeAuthor(id: string) {
  await api.delete(`/authors/${id}`);
}
