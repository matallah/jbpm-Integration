import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TasksDetailComponent } from './tasks-detail.component';

describe('Tasks Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TasksDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TasksDetailComponent,
              resolve: { tasks: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(TasksDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load tasks on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TasksDetailComponent);

      // THEN
      expect(instance.tasks).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
