import { describe, it, expect, vi, beforeEach } from 'vitest';
import type { Mock } from 'vitest';
import { useBookPage } from './BookPage';

import { createBook, listAllBook, removeBook, updateBook } from 'src/services/bookService';
import { listAllAuthor } from 'src/services/authorService';
import { listAllGenre } from 'src/services/genreService';
import type { AuthorType } from 'src/types/authorType';
import type { GenreType } from 'src/types/genreType';

vi.mock('src/services/bookService', () => ({
  listAllBook: vi.fn(),
  createBook: vi.fn(),
  updateBook: vi.fn(),
  removeBook: vi.fn(),
}));

vi.mock('src/services/authorService', () => ({
  listAllAuthor: vi.fn(),
}));

vi.mock('src/services/genreService', () => ({
  listAllGenre: vi.fn(),
}));

vi.mock('src/services/messageService', () => ({
  notifyConfirm: vi.fn().mockResolvedValue(true),
  notifyError: vi.fn(),
  notifySuccess: vi.fn(),
  notifyWarning: vi.fn(),
}));

describe('useBookPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('carrega livros com loadItems', async () => {
    (listAllBook as Mock).mockResolvedValue([{ id: '1', title: 'Livro 1', author: { id: '1', name: 'Autor 1' }, genre: { id: '1', description: 'Ficção' }, createdAt: '2024-01-01' }]);
    const { loadItems, items } = useBookPage();
    await loadItems();
    expect(items.value.length).toBe(1);
    expect(items.value[0]?.title).toBe('Livro 1');
  });

  it('carrega opções de seleção com loadSelectOptions', async () => {
    (listAllAuthor as Mock).mockResolvedValue([{ id: '1', name: 'Autor 1' }]);
    (listAllGenre as Mock).mockResolvedValue([{ id: '1', description: 'Ficção' }]);
    const { loadSelectOptions, authors, genres } = useBookPage();
    await loadSelectOptions();
    expect(authors.value.length).toBe(1);
    expect(authors.value[0]?.name).toBe('Autor 1');
    expect(genres.value.length).toBe(1);
    expect(genres.value[0]?.description).toBe('Ficção');
  });

  it('cria livro com onSubmit', async () => {
    const newBook = { id: null, title: 'Novo Livro', author: { id: '1', name: 'Autor 1' } as AuthorType, genre: { id: '1', description: 'Ficção' } as GenreType, createdAt: null };
    (createBook as Mock).mockResolvedValue({ ...newBook, id: '2' });
    const { form, submitAndClose } = useBookPage();
    form.value = { ...newBook };
    await submitAndClose();
    expect(createBook).toHaveBeenCalledWith(newBook);
  });

  it('atualiza livro com onSubmit', async () => {
    const updatedBook = { id: '1', title: 'Livro Atualizado', author: { id: '1', name: 'Autor 1', createdAt: new Date() } as AuthorType, genre: { id: '1', description: 'Ficção', createdAt: new Date() } as GenreType, createdAt: null };
    (updateBook as Mock).mockResolvedValue(updatedBook);
    const { form, submitAndClose } = useBookPage();
    form.value = { ...updatedBook };
    await submitAndClose();
    expect(updateBook).toHaveBeenCalledWith('1', updatedBook);
  });

  it('remove livro com deleteItem', async () => {
    (removeBook as Mock).mockResolvedValue({});
    (listAllBook as Mock).mockResolvedValue([]);
    const { items, deleteItem, loadItems } = useBookPage();
    items.value = [{ id: '1', title: 'Livro 1', author: { id: '1', name: 'Autor 1', createdAt: new Date() }, genre: { id: '1', description: 'Ficção', createdAt: new Date() }, createdAt: null }];
    await deleteItem(0);
    expect(removeBook).toHaveBeenCalledWith('1');
    await loadItems();
  });
});
