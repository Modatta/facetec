export interface FacetecPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  setup(): Promise<{ value: string }>;
}
